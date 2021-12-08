package core

import cats.{Alternative, ApplicativeError, Foldable, Id, Monad, Traverse}

object FoldableFilterError {

  implicit class FoldableFilterErrorOps[F[_] : Foldable : Alternative, G[_] : Traverse, E, A](fga: F[G[A]])(implicit AE: ApplicativeError[G, E]) {

    /** Flattens the errors and returns only the successful values. Allows the caller to specify a
     * monadic representation of an effect describing the error handling. */
    def filterErrorM[M[_] : Monad](errHandler: F[E] => M[Unit]): M[F[A]] = {
      // partitionBifold requires the return type of the function to be a BiFoldable[_,_]
      // AE.attemptT returns an EitherT[G,E,A]
      // EitherT[G,E,A] is a BiFoldable[_,_] if G[_] is a Traverse.
      val partition: (F[E], F[A]) = Foldable[F].partitionBifold(fga)((possibleFailure: G[A]) => AE.attemptT(possibleFailure))
      val processedError: M[Unit] = errHandler(partition._1)
      Monad[M].flatMap(processedError)(_ => Monad[M].pure(partition._2))
    }

    val unitId: Id[Unit] = ()

    /** Flattens the errors and returns only the successful values. Allows the caller to specify the processing of the errors. */
    def filterError(errHandler: F[E] => Unit): F[A] = filterErrorM(errHandler.andThen(_ => unitId))

    /** Flattens the errors and returns only the successful values. The errors are silently discarded. */
    def filterError: F[A] = filterError(_ => ())
  }

}

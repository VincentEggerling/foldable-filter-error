package core

import cats.{Alternative, ApplicativeError, Foldable, Id, Monad, Traverse}

object FoldableFilterError {

  implicit class FoldableFilterErrorOps[F[_] : Foldable : Alternative, G[_] : Traverse, E, A](fga: F[G[A]])(implicit AE: ApplicativeError[G, E]) {

    /** Flattens the errors and returns only the successful values. Allows the caller to specify a
     * monadic representation of an effect describing the error handling. */
    def filterErrorM[M[_] : Monad](f: F[E] => M[Unit]): M[F[A]] = {
      val partition: (F[E], F[A]) = Foldable[F].partitionBifold(fga)((possibleFailure: G[A]) => AE.attemptT(possibleFailure))
      val processedError: M[Unit] = f(partition._1)
      Monad[M].flatMap(processedError)(_ => Monad[M].pure(partition._2))
    }

    val unitId: Id[Unit] = ()

    /** Flattens the errors and returns only the successful values. Allows the caller to specify the processing of the errors. */
    def filterError(f: F[E] => Unit): F[A] = filterErrorM(f.andThen(_ => unitId))

    /** Flattens the errors and returns only the successful values. The errors are silently discarded. */
    def filterError: F[A] = filterError(_ => ())
  }

}

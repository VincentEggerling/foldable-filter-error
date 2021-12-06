# foldable-filter-error
# Intro

Mini-lib that provides a generic and easy way to filter collections of error. It provides the `.filterError()` family of methods for
all Foldable structures that contains some error containers. For example:

```
val res1: List[Int] = List(Success(1), Failure(new Exception("oops")), Success(3)).filterError
val res2: List[Int] = List(Some(1), None, Some(3)).filterError
val res3: Option[Int] = Some(Success(1)).filterError
```

## Why not flatten() or sequence() ?
The typical way of handling a List of Option's or a List of Try's is to either flatten the list or call some *sequence* like method. Each way
has a downside: 
- `flatten` will **silently** discard any errors.
- `sequence` will only return a success if all element in the collection are successful. This might be the behavior you want in some situation, but
  not always.

This brings us back to our `filterError` and in particular it's two variants (Here we assume a `List[Try[X]]`)
- `filterError(List[Throwable] => Unit)`
- `filterErrorM[M[_]: Monad](List[Throwable] => M[Unit])`

they allows the caller to flatten the error **and** provide a way to process the errors either in a side-effecty way, or purely with a monadic 
representation of the side-effect (Using cats IO, or ZIO, etc...)

Checkout the examples package for toy-examples (with side-effects and pure with ZIO) of how to chain the calls to `filterError` in a real world context.

## Most common combinations of collections and errors:
You can use the filterError methods on many different containers. 
- Collections: List, Vector, Option
- Errors: Option, Try, Either










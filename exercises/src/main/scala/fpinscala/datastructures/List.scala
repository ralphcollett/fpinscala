package fpinscala.datastructures

import scala.annotation.tailrec

sealed trait List[+A] // `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing] // A `List` data constructor representing the empty list
/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List { // `List` companion object. Contains functions for creating and working with lists.
  def sum(ints: List[Int]): Int = ints match { // A function that uses pattern matching to add up a list of integers
    case Nil => 0 // The sum of the empty list is 0.
    case Cons(x,xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val matchResult = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h,t) => Cons(h, append(t, a2))
    }

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]) =
    foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar


  def tail[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(_, xs) => xs
  }

  def setHead[A](l: List[A], newHead: A): List[A] = l match {
    case Nil => Nil
    case Cons(head, tail) => Cons(newHead, tail)
  }

  def drop[A](l: List[A], n: Int): List[A] = {
    if (n < 1) l else l match {
      case Nil => Nil
      case Cons(head, tail) => drop(tail, n - 1)
    }
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(head, tail) if f(head) => dropWhile(tail, f)
    case ll => ll
  }

  def init[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(h, Nil) => Nil
    case Cons(h, tail) => Cons(h, init(tail))
  }

  def length[A](l: List[A]): Int = foldRight(l, 0)((_, acc) => acc + 1)

  @tailrec
  def foldLeft[A,B](l: List[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(head, tail) => foldLeft(tail, f(z, head))(f)
  }

  def sumFoldLeft(ns: List[Int]) =
    foldLeft(ns, 0)(_ + _)

  def productFoldLeft(ns: List[Double]) =
    foldLeft(ns, 1.0)(_ * _)

  def lengthFoldLeft[A](l: List[A]): Int =
    foldLeft(l, 0)((acc, _) => acc + 1)

  def reverse[A](l: List[A]): List[A] =
    foldLeft(l, Nil: List[A])((acc, b) => Cons(b, acc))

  def appendFoldRight[A](l: List[A], ll: List[A]): List[A] =
    foldRight(l, ll)(Cons(_,_))

  def concatenate[A](l: List[List[A]]): List[A] = {
    foldRight(l, Nil: List[A])(appendFoldRight)
  }

  def map[A,B](l: List[A])(f: A => B): List[B] = sys.error("todo")

  def mkString[A](l: List[A]): String = {
    def toScalaList(ll: List[A]): scala.List[A] = ll match {
      case Nil => scala.List.empty
      case Cons(head, tail) => head :: toScalaList(tail)
    }

    toScalaList(l).mkString
  }
}

object TestList {

  import List._

  def main(args: Array[String]): Unit = {
    println(matchResult)

    println(mkString(tail(List(1, 2, 3, 4, 5))))
    println(mkString(setHead(List(1, 2, 3, 4, 5), 6)))
    println(mkString(drop(List(1, 2, 3, 4, 5), 2)))
    println(mkString(dropWhile[Int](List(1, 2, 3, 4, 5), _ < 4)))
    println(mkString(init(List(1, 2, 3, 4, 5))))
    println(mkString(foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_))))
    println(length(List(1, 2, 3, 4, 5)))
    println(foldLeft(List(1, 2, 3, 4, 5), "0")((b, acc) => s"$acc, $b"))
    println(sumFoldLeft(List(1, 2, 3, 4, 5)))
    println(productFoldLeft(List(1, 2, 3, 4, 5)))
    println(lengthFoldLeft(List(1, 2, 3, 4, 5)))
    println(mkString(reverse(List(1, 2, 3, 4, 5))))
    println(mkString(appendFoldRight(List(1, 2, 3, 4, 5), List(7, 8, 9))))
    println(mkString(concatenate(List(List(1, 2, 3, 4, 5), List(7, 8, 9), List(10, 11, 12)))))
  }
}

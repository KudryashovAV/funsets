package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  test("it does not contain 100") {
    assert(!contains({(x: Int) => x > 0}, -100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1: _root_.funsets.FunSets.FunSet = singletonSet(1)
    val s2: _root_.funsets.FunSets.FunSet = singletonSet(2)
    val s3: _root_.funsets.FunSets.FunSet = singletonSet(3)
    val s4: _root_.funsets.FunSets.FunSet = singletonSet(1)
    val s5: _root_.funsets.FunSets.FunSet = singletonSet(4)
    val positiveNumbersSet: _root_.funsets.FunSets.FunSet = union(singletonSet(1), singletonSet(300))
    val negativeNumbersSet: _root_.funsets.FunSets.FunSet = union(singletonSet(-10), singletonSet(-99))
    val setPositiveAndNegativeNumbers: _root_.funsets.FunSets.FunSet = union(positiveNumbersSet, negativeNumbersSet)
    val evenNumbersSet: _root_.funsets.FunSets.FunSet = union(singletonSet(4), singletonSet(6))
    val oddNumbersSet: _root_.funsets.FunSets.FunSet = union(singletonSet(3), singletonSet(9))
    val evenAndOddNumbersSet: _root_.funsets.FunSets.FunSet = union(evenNumbersSet, oddNumbersSet)
  }

  test("SingletonSet function. SingletonSet(1) contains one") {
    new TestSets:
      assert(contains(s1, 1), "Singleton")
  }

  test("SingletonSet function. SingletonSet(1) contains two") {
    new TestSets:
      assert(!contains(s1, 2), "Singleton does not contain 2")
  }

  test("Union function") {
    new TestSets:
      val s: _root_.funsets.FunSets.FunSet = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("Intersect function") {
    new TestSets:
      val intersection1: _root_.funsets.FunSets.FunSet = intersect(s1, s2)
      assert(!contains(intersection1, 1), "Intersect 1 between singletonSet(1) and singletonSet(2)")
      val intersection2: _root_.funsets.FunSets.FunSet = intersect(s1, s4)
      assert(contains(intersection2, 1), "Intersect 1 between singletonSet(1) and singletonSet(4)")
      assert(!contains(intersection2, 2), "Intersect 2 between singletonSet(1) and singletonSet(4)")
  }

  test("Diff function") {
    new TestSets:
      val difference1: _root_.funsets.FunSets.FunSet = diff(s1, s2)
      assert(contains(difference1, 1), "Diff 1 between singletonSet(1) and singletonSet(2)")
      val difference2: _root_.funsets.FunSets.FunSet = diff(s1, s4)
      assert(!contains(difference2, 1), "Diff 2 between singletonSet(1) and singletonSet(4)")
  }

  test("Filter function") {
    new TestSets:
      val filterSet1: _root_.funsets.FunSets.FunSet = filter(s1, {(elem: Int) => elem < 2})
      assert(contains(filterSet1, 1))

      val filterSet2: _root_.funsets.FunSets.FunSet = filter(s3, {(elem: Int) => elem > 5})
      assert(!contains(filterSet2, 3))
  }

  test("Forall function") {
    new TestSets:
      assert(forall(positiveNumbersSet, {(elem: Int) => elem > 0}))
      assert(forall(negativeNumbersSet, {(elem: Int) => elem < 0}))
      assert(!forall(setPositiveAndNegativeNumbers, {(elem: Int) => elem < 0}))
      assert(forall(evenNumbersSet, {(elem: Int) => (elem % 2) == 0}))
      assert(forall(oddNumbersSet, {(elem: Int) => (elem % 2) != 0}))
      assert(!forall(evenAndOddNumbersSet, {(elem: Int) => (elem % 2) == 0}))
  }

  test("Exists function") {
    new TestSets:
      assert(exists(setPositiveAndNegativeNumbers, {(elem: Int) => elem > 0}))
      assert(exists(evenAndOddNumbersSet, {(elem: Int) => (elem % 2) == 0}))
  }

  test("Map function") {
    new TestSets:
      val mapEvenSetToOdd: _root_.funsets.FunSets.FunSet = map(evenNumbersSet, { (elem: Int) => elem + 1})
      assert(contains(mapEvenSetToOdd, 5) && contains(mapEvenSetToOdd, 7))

      val mapOddSetToEven: _root_.funsets.FunSets.FunSet = map(oddNumbersSet, { (elem: Int) => elem * 2})
      assert(forall(mapOddSetToEven, {(elem: Int) => (elem % 2) == 0}))
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
  
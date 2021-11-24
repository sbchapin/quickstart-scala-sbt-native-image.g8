package $package$.spec

import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funspec.AsyncFunSpec

/** This should be the base-level class to guarantee congruency among specs across your tests.
  *
  * It should change often early, and rarely lately.
  *
  * To see some great Scalatest examples using FunSpec, check out this link:
  * http://www.scalatest.org/at_a_glance/FunSpec
  *
  * Scalatest has great documentation -- check it out here:
  * http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FunSpec
  *
  * @author
  *   sbchapin
  * @since 11/11/19.
  */
trait BaseSpec extends AsyncFunSpec with AsyncIOSpec {
  // feel free to put test helpers here
}

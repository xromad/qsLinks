package com.bjs.test

import org.mockito.scalatest.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

//todo: this will become the foundation of testing my classes

class MyDependency {
  def doSomething(name: String): String = {
    s"$name did something wrong!"
  }
}

class MyClass(mc: MyDependency) {
  def doIt(name: String): String = {
    mc.doSomething(name)
  }
}

class MockitoPracticeTest extends AnyFlatSpec
  with Matchers
  with MockitoSugar {
  "mocked classes" should "return what is expected" in {
    val myMock = mock[MyDependency]
    when(myMock.doSomething(any[String])) thenReturn "Someone did something right!"
    val mockClass = new MyClass(myMock)

    mockClass.doIt("Brent") shouldBe "Someone did something right!"
    mockClass.doIt("Frank") shouldBe "Someone did something right!"
  }

}

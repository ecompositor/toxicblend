package org.toxicblend.tests

import org.scalatest._
import org.toxicblend.operations.meshgenerator.vecmath.SutherlandHodgemanClipper
import org.toxicblend.ToxicblendException
import org.toxicblend.operations.meshgenerator.vecmath.ImmutableVec2D
import org.toxicblend.operations.meshgenerator.vecmath.Vec2D
import org.toxicblend.operations.meshgenerator.vecmath.FiniteLine2D
import org.toxicblend.operations.meshgenerator.vecmath.Polygon2D

class LineIntersectionTest extends FlatSpec with Matchers {
  
  val doubleTolerance = 0.0001d
  
  /** 
   *  y = x
   *        /
   *       /
   * -----/-------
   *     /
   *    /
   */
  "LineIntersectionTest-1" should "work just fine" in {
    val iLine = new FiniteLine2D(new ImmutableVec2D(-.1f,-.1f), new ImmutableVec2D(.1f,.1f))
    for( y<- -100 to 100) {
      val fromV = new ImmutableVec2D(-100,y)
      val toV = new ImmutableVec2D(100,y)
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x should be (y.toDouble plusOrMinus doubleTolerance ) 
      rv.y should be (y.toDouble plusOrMinus doubleTolerance)
    }
  }
  
  
  /** 
   *  y = -x
   *    \  
   *     \ 
   * -----\-----
   *       \
   *        \
   */
  "LineIntersectionTest-2" should "work just fine" in {
    val iLine = new FiniteLine2D(new ImmutableVec2D(-.1f,.1f), new ImmutableVec2D(.1f,-.1f))
    for( y<- -100 to 100) {
      val fromV = new ImmutableVec2D(-100,y)
      val toV = new ImmutableVec2D(100,y)
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x should === (-y.toDouble plusOrMinus doubleTolerance)
      rv.y should === (y.toDouble plusOrMinus doubleTolerance)
    }
  }
  
  /** 
   *  x = 0
   *    |  
   *    |  
   * ---|---
   *    |
   *    |
   */
  "LineIntersectionTest-3" should "work just fine" in {
    val iLine = new FiniteLine2D(new ImmutableVec2D(0f,.1f), new ImmutableVec2D(0f,-.1f))
    for( y<- -100 to 100) {
      val fromV = new ImmutableVec2D(-10,y)
      val toV = new ImmutableVec2D(10,y)
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x should === (0d plusOrMinus doubleTolerance)
      rv.y should === (y.toDouble plusOrMinus doubleTolerance)
    }
  }
  
  /** 
   *  x = 0
   *    |  
   *    |  
   * ---|---
   *    |
   *    |
   */
  "LineIntersectionTest-4" should "work just fine" in {
    val iLine = new FiniteLine2D(new ImmutableVec2D(0f,-.1f), new ImmutableVec2D(0f,.1f))
    for( y<- -100 to 100) {
      val fromV = new ImmutableVec2D(-10,y)
      val toV = new ImmutableVec2D(10,y)
      
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x should === (0d plusOrMinus doubleTolerance)
      rv.y should === (y.toDouble plusOrMinus doubleTolerance)
    }
  }
  
  /** 
   *  y = x
   *        / ----
   *       /
   * -----/-------
   *     /
   *    /
   *
  "LineIntersectionTest-5" should "not find any intersections" in {
    val iLine = new FiniteLine2D(Vec2D(-.1f,-.1f), Vec2D(.1f,.1f))
    for( y<- -100 to 100) {
      val fromV = Vec2D(101,y)
      val toV = Vec2D(201,y)
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x.isNaN should be (true)
      rv.y.isNaN should be (true)
    }
  }
  
  / ** 
   *  y = x
   *  ----  / 
   *       /
   * -----/-------
   *     /
   *    /
   *
  "LineIntersectionTest-6" should "not find any intersections" in {
    val iLine = new FiniteLine2D(Vec2D(-.1f,-.1f), Vec2D(.1f,.1f))
    for( y<- -100 to 100) {
      val fromV = Vec2D(-101,y)
      val toV = Vec2D(-201,y)
      val rv = SutherlandHodgemanClipper.singleton.intersection(iLine.a, iLine.b, fromV, toV)
      rv.x.isNaN should be (true)
      rv.y.isNaN should be (true)
    }
  }
  
  * */
  
  "LineIntersectionTest-7" should "not find any intersections" in {
    val p = new Polygon2D(IndexedSeq((0,0), (10,0), (0,10)).map(v=>Vec2D(v._1,v._2)))
    p.isClockwise should be (false)
    p.isSelfIntersecting should be (false)
  }
}
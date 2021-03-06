package org.toxicblend.tests


import org.scalatest._
import org.toxicblend.vecmath.SutherlandHodgemanClipper
import org.toxicblend.vecmath.SutherlandHodgemanRectangularClipper
import org.toxicblend.ToxicblendException
import org.toxicblend.vecmath.ImmutableVec2D
import org.toxicblend.vecmath.MutableVec2D
import org.toxicblend.vecmath.Vec2D
import org.toxicblend.vecmath.AABB2D
import org.toxicblend.vecmath.FiniteLine2D
import org.toxicblend.vecmath.Polygon2D
//import toxi.geom.{Polygon2D=>TPolygon2D}
import org.toxicblend.util.Payload
import org.toxicblend.util.CyclicTree

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._
import Vec2DMatcher._

class SutherlandHodgemanClipperTest extends FlatSpec with Matchers {
  
  val tolerance = 0.0001d
  
  def indexByHeading(seq:IndexedSeq[Vec2D] ) : IndexedSeq[Vec2D] = {
    //println("seq.isClockwise=" + Polygon2D.isClockwise(seq))
    //println("seq.isSelfIntersecting=" + Polygon2D.isSelfIntersecting(seq))
    val centroid = Polygon2D.getCentroid(seq)
    //println("centroid=" + centroid)
    val angleSequence = seq.map(v => new Payload(v.sub(centroid).heading, 0, v) )
    //println(angleSequence.map(p=>p.pos).mkString(","))
    //println(angleSequence.map(p=>"" + p.pos + "@" + p.angle*180d/math.Pi).mkString(","))
    //println("isClockwise2=" + Polygon2D.isClockwise(angleSequence.map(p=>p.pos) ))
    val rv = CyclicTree.inOrder(angleSequence)._1.map( p => p.pos )
    //println(rv.map(v=>"" + v + "@" + v.heading*180d/math.Pi).mkString(","))
    rv
  }
  
  /** 
   *   ____  /          ____ 
   *  |    |/          |    |
   *  |    /       =>  |   /
   *  |   /|           |  /
   *  |_/__|           |_/
   *   /   
   * /
  "SutherlandHodgemanClipperTest-1" should "clip just fine" in {
    
    val p0 = Vec2D(2,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(2,1)
    val i0 = Vec2D(2,0)
    val i1 = Vec2D(1,-1)
    
    val iLine = new FiniteLine2D(i1, i0)
    val polygon = ArrayBuffer(p0, p1, p2, p3)
    
    val clipped = indexByHeading(SutherlandHodgemanClipper.clip(polygon, iLine, Polygon2D.ε).toIndexedSeq)
    //println(clipped.mkString(","))
    clipped.size should be (5)
    clipped.get(0) should be (p2)
    clipped.get(1) should be (p3)
    clipped.get(2) should be (i0)
    clipped.get(3) should be (i1)
    clipped.get(4) should be (p1)
  }*/
  
  /** 
   *   ____  /             /
   *  |    |/             /|
   *  |    /       =>    / |
   *  |   /|            /  |
   *  |_/__|           /___|
   *   /              /               
   * /
  "SutherlandHodgemanClipperTest-2" should "clip just fine" in {
    
    val p0 = Vec2D(2,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(2,1)
    val i0 = Vec2D(1,1)
    val i1 = Vec2D(-1,0)
    
    val iLine = new FiniteLine2D(i0, i1)
    val polygon = ArrayBuffer(p0, p1, p2, p3)
    val clipper = new SutherlandHodgemanClipper
    
    val clipped = indexByHeading(clipper.clipPolygon(polygon, iLine, Polygon2D.ε ).toIndexedSeq)
    //println("SutherlandHodgemanClipperTest-2: polygon=" + clipped.mkString(","))
    //println("SutherlandHodgemanClipperTest-2: clipline=" + iLine )
    //println("SutherlandHodgemanClipperTest-2: clipped=" + clipped.mkString(","))
    //println("SutherlandHodgemanClipperTest-2: expected=" + Array(i1,i0,p3,p0,p1).mkString(","))
    clipped.size should be (5)
    clipped.get(0) should be (i1)
    clipped.get(1) should be (i0)
    clipped.get(2) should be (p3)
    clipped.get(3) should be (p0)
    clipped.get(4) should be (p1)
  } */
    
  /**
   * Rectangular, clockwise clipping 
   
  "SutherlandHodgemanClipperTest-3" should "clip just fine" in {
    
    val p0 = Vec2D(1,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(1,1)
    val trace = true
    
    var polygon:IndexedSeq[Vec2D] = ArrayBuffer(p0, p1, p2, p3).map(v=>v.scale(2))
    val clipper = new SutherlandHodgemanClipper
    
    if (trace) {
      println("SutherlandHodgemanClipperTest-3 polygon=" + polygon)
      println
    }
    
    var iLine = new FiniteLine2D(p0, p1)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p1, p2)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p2, p3)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p3, p0)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    val rv = indexByHeading(polygon)
    if (trace) {
      println("rv=" + rv)
      println
    }
    rv.size should be (4)
    rv.get(0) should be (p2)
    rv.get(1) should be (p3)
    rv.get(2) should be (p0)
    rv.get(3) should be (p1)
  }
  */
  
  /**
   * Rectangular, counter-clockwise clipping 
   
  "SutherlandHodgemanClipperTest-4" should "clip just fine" in {
    
    val p0 = Vec2D(1,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(1,1)
    val trace = true
    
    var polygon:IndexedSeq[Vec2D] = ArrayBuffer(p2, p1, p0, p3).map(v=>v.scale(10))
    val clipper = new SutherlandHodgemanClipper
    
    if (trace) {
      println("SutherlandHodgemanClipperTest-4 polygon=" + polygon)
      println
    }
    
    var iLine = new FiniteLine2D(p0, p1)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p1, p2)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p2, p3)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    iLine = new FiniteLine2D(p3, p0)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    if (trace) {
      println("iLine=" + iLine)
      println("polygon=" + polygon)
      println
    }
    polygon.size should be (4)
    
    val rv = indexByHeading(polygon.toIndexedSeq)
    println(rv)
    rv.size should be (4)
    rv.get(0) should be (p1)
    rv.get(1) should be (p0)
    rv.get(2) should be (p3)
    rv.get(3) should be (p2)
  }
  */
  /**
   *  Rectangular, counter-clockwise, coincident clipping 
   
  "SutherlandHodgemanClipperTest-5" should "clip counter-clockwise, coincident polygons" in {
    
    val p0 = Vec2D(1,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(1,1)
    
    
    var polygon:IndexedSeq[Vec2D] = ArrayBuffer(p2.scale(10), p1.scale(10), p0.scale(10), p3.scale(10))
    val clipper = new SutherlandHodgemanClipper
    
    //println(polygon)
    //println
    
    var iLine = new FiniteLine2D(p0, p1)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p1, p2)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p2, p3)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p3, p0)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    val rv = indexByHeading(polygon.toIndexedSeq)
    //println(rv)
    rv.size should be (4)
    rv.get(0) should be (p1)
    rv.get(1) should be (p0)
    rv.get(2) should be (p3)
    rv.get(3) should be (p2)
  }
  */
  /**
   *  Rectangular, coincident, clockwise clipping 
   
  "SutherlandHodgemanClipperTest-6" should "clip clockwise, coincident polygons" in {
    
    val p0 = Vec2D(1,-1)
    val p1 = Vec2D(-1,-1)
    val p2 = Vec2D(-1,1)
    val p3 = Vec2D(1,1)
    
    var polygon:IndexedSeq[Vec2D] = ArrayBuffer(p0, p1, p2, p3)
    val clipper = new SutherlandHodgemanClipper
    
    //println(polygon)
    //println
    
    var iLine = new FiniteLine2D(p0, p1)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p1, p2)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p2, p3)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    iLine = new FiniteLine2D(p3, p0)
    polygon = clipper.clipPolygon(polygon, iLine, Polygon2D.ε)
    //println(iLine)
    //println(polygon)
    //println
    
    val rv = indexByHeading(polygon.toIndexedSeq)
    //println(rv)
    rv.size should be (4)
    rv.get(0) should be (p2)
    rv.get(1) should be (p3)
    rv.get(2) should be (p0)
    rv.get(3) should be (p1)
  }
  */
  /**
   *  Rectangular clipping from http://rosettacode.org/wiki/Sutherland-Hodgman_polygon_clipping
   */
  "SutherlandHodgemanClipperTest-7" should "clip like rosettacode.org" in {
      
    val polygon = ArrayBuffer((50,150),(200,50),(350,150),(350,300),(250,300),(200,250),(150,350),(100,250),(100,200)).map(p=>Vec2D(p._1,p._2))
    val clipEdges = ArrayBuffer((100,100),(300,100),(300,300),(100,300)).map(p=>Vec2D(p._1,p._2))
    val clipper = new SutherlandHodgemanClipper(polygon.size, Polygon2D.ε)

    val clipped = clipper.clip(polygon, clipEdges)
    
    val correctAnswer = Array((100.000000, 116.666667),
                              (125.000000, 100.000000),
                              (275.000000, 100.000000),
                              (300.000000, 116.666667),
                              (300.000000, 300.000000),
                              (250.000000, 300.000000),
                              (200.000000, 250.000000),
                              (175.000000, 300.000000),
                              (125.000000, 300.000000),
                              (100.000000, 250.000000)).map(p=>Vec2D(p._1.toFloat, p._2.toFloat))
    clipped.size should be (correctAnswer.size)
    (0 until clipped.size).foreach(i=>{
      clipped(i).x should be ( correctAnswer(i).x plusOrMinus tolerance)
      clipped(i).y should be ( correctAnswer(i).y plusOrMinus tolerance)
    })
    
    val rectClipper = new SutherlandHodgemanRectangularClipper(polygon.size, Polygon2D.ε)
    val aabb = AABB2D(clipEdges)
    val rectClipped = rectClipper.clip(polygon, aabb)
    rectClipped.size should be (correctAnswer.size)
    (0 until rectClipped.size).foreach(i=> rectClipped(i) should equal2d (correctAnswer(i),tolerance) )
    
  }
}
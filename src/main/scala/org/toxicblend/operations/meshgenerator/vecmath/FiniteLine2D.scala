package org.toxicblend.operations.meshgenerator.vecmath

sealed abstract class Intersection
sealed case class SimpleIntersection(val p:Vec2D) extends Intersection
sealed case class CoincidentIntersection(val a:Vec2D, val b:Vec2D) extends Intersection

class FiniteLine2D(val a:Vec2D, val b:Vec2D) {
  
  def getDirection: Vec2D = b.sub(a).normalize
  
  def intersectLine(l:FiniteLine2D): Option[Intersection] = {
    val denom = (l.b.y - l.a.y) * (b.x - a.x) - (l.b.x - l.a.x) * (b.y - a.y)
    val na = (l.b.x - l.a.x) * (a.y - l.a.y) - (l.b.y - l.a.y) * (a.x - l.a.x)
    val nb = (b.x - a.x) * (a.y - l.a.y) - (b.y - a.y) * (a.x - l.a.x)

    if (denom != 0.0) {
      val ua = na / denom
      val ub = nb / denom
      if (ua >= 0d && ua <= 1d && ub >= 0d && ub <= 1d) {
        val intersection = a.interpolateTo(b, ua)
        println("" + this + " intersects " + l + " at " + intersection)
        Option(new SimpleIntersection(intersection))
      } else return None
    } else if (na == 0.0 && nb == 0.0) {
      println("" + this + " is coincident with " + l )
      
      val aTobSquared = a.distanceToSquared(b)
      val aToLaSquared = a.distanceToSquared(l.a)
      val aToLbSquared = a.distanceToSquared(l.b)
      
      println("aTobSquared=" + aTobSquared)
      println("aToLaSquared=" + aToLaSquared)
      println("aToLbSquared=" + aToLbSquared)
      
      if (aTobSquared < aToLaSquared && aTobSquared < aToLbSquared) 
        return None // coincident non-intersecting
      
      if (aTobSquared >= aToLaSquared && aTobSquared >= aToLbSquared){
        if (aToLaSquared > aToLbSquared) return Option(new CoincidentIntersection(l.b,l.a))
        else return Option(new CoincidentIntersection(l.a,l.b))
      }
          
      if (aToLaSquared < aTobSquared ) return Option(new CoincidentIntersection(l.a,b))
      if (aToLbSquared < aTobSquared ) return Option(new CoincidentIntersection(l.b,b))
      if (aToLaSquared == aTobSquared ) return Option(new SimpleIntersection(l.a))
      if (aToLbSquared == aTobSquared ) return Option(new SimpleIntersection(l.b))
      
      assert(false, "no more options left")
      None
    } else return None
  }
  def intersects(that:FiniteLine2D): Boolean = FiniteLine2D.intersects(a, b, that.a, that.b)
  def sqrDistanceToPoint(p:Vec2D) = FiniteLine2D.sqrDistanceToPoint(p,a,b)
  def distanceToPoint(p:Vec2D) = math.sqrt(FiniteLine2D.sqrDistanceToPoint(p,a,b))
  
  override def toString = a.toString + "->" + b.toString
}

object FiniteLine2D {
  
  def apply(s1:Vec2D, s2:Vec2D) = new FiniteLine2D(s1, s2)
  
  @inline def sqrDistanceToPoint(p:Vec2D, s1:Vec2D, s2:Vec2D, ε:Double=Polygon2D.ε): Double = {
    if (s1.=~=(s2, Polygon2D.ε)) s1.distanceToSquared(p)
    else {
      val u = ((p.x-s1.x)*(s2.x-s1.x)+(p.y-s1.y)*(s2.y-s1.y))/s1.distanceToSquared(s2)
      if (u <= 0) {
        p.distanceToSquared(s1)
      } else if (u >= 1d) {
        p.distanceToSquared(s2)
      } else {
        val x = s1.x + u*(s2.x-s1.x)
        val y = s1.y + u*(s2.y-s1.y)
        p.distanceToSquared(x,y)
      }
    }
  }
  
  @inline def intersects(aa:Vec2D, ab:Vec2D, ba:Vec2D, bb:Vec2D): Boolean = {
    if (Vec2D.ccw(aa, ab, ba) * Vec2D.ccw(aa, ab, bb) > 0) return false
    if (Vec2D.ccw(ba, bb, aa) * Vec2D.ccw(ba, bb, ab) > 0) return false
    true
  }
  
}
package org.toxicblend.operations.zadjust

import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback
import com.bulletphysics.collision.dispatch.CollisionWorld.LocalRayResult
import com.bulletphysics.linearmath.VectorUtil
import javax.vecmath.Vector3d
import javax.vecmath.Point2d
import javax.vecmath.Point3d
import com.bulletphysics.linearmath.Point3dE
import com.bulletphysics.linearmath.Vector3dE

class ClosestRayResultCallback(val minZ:Double,val maxZ:Double) extends RayResultCallback {
  val rayFromWorld = new Vector3dE; rayFromWorld.z = maxZ
  val rayToWorld = new Vector3dE; rayToWorld.z = minZ
  val hitPointWorld = new HitPointWorld
     
  /**
   * callback from jbullet on ray collision
   */
  override def addSingleResult(rayResult:LocalRayResult, normalInWorldSpace:Boolean):Double = {
    closestHitFraction = rayResult.hitFraction      
    hitPointWorld.triangleIndex = rayResult.localShapeInfo.triangleIndex
    VectorUtil.setInterpolate3(hitPointWorld.collisionPoint, rayFromWorld, rayToWorld, closestHitFraction)
    //searchstate.currentC.setCollision(hitPointWorld, rayResult.localShapeInfo.triangleIndex)
    //val triangle = searchstate.collisionWrapper.models(0).getFaces(searchstate.currentC.triangleIndex).toIndexedSeq.map(i => searchstate.collisionWrapper.models(0).getVertices(i))
    //TrianglePlaneIntersection.trianglePlaneIntersection(triangle, searchstate.segmentPlane, searchstate.currentC.collisionPoint, searchstate.directionNormalized, searchstate.currentC)
    closestHitFraction
  }
  
  @inline def hasResult = closestHitFraction < 1d
  /**
   * returns the collision result as a reference to an internal reused variable
   */
  @inline def getResult = {
    if (!hasResult) {
      hitPointWorld.collisionPoint.set(rayFromWorld.x, rayFromWorld.y, minZ)
      // hitpointWorld.triangleIndex = -1 should already be done
    }
    hitPointWorld
  }
  
  def resetForReuse(samplePoint:Point3d) = {
    rayFromWorld.x = samplePoint.x
    rayFromWorld.y = samplePoint.y
    rayToWorld.x = samplePoint.x
    rayToWorld.y = samplePoint.y
    
    hitPointWorld.triangleIndex = -1
    closestHitFraction = 1d
  }
}
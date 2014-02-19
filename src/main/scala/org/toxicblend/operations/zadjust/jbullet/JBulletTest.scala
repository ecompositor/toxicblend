package org.toxicblend.operations.zadjust.jbullet;

import toxi.geom.mesh.TriangleMesh
import toxi.geom.Vec3D
import toxi.geom.ReadonlyVec3D
import com.bulletphysics.util.ObjectArrayList
import com.bulletphysics.collision.broadphase.BroadphaseInterface
import com.bulletphysics.collision.broadphase.AxisSweep3_32
import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray
import com.bulletphysics.collision.shapes.ConeShapeZ
import com.bulletphysics.collision.shapes.ConvexShape
import com.bulletphysics.collision.shapes.UniformScalingShape
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.CollisionFlags
import com.bulletphysics.collision.dispatch.CollisionWorld
import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback
import com.bulletphysics.collision.dispatch.CollisionWorld.LocalRayResult
import com.bulletphysics.linearmath.Transform
import com.bulletphysics.linearmath.DefaultMotionState;
import org.toxicblend.typeconverters.Mesh3DConverter;
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.MutableList
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.vecmath.Vector3f
import com.bulletphysics.linearmath.VectorUtil

object JBulletTest {
 
  def main(args: Array[String]): Unit = {
   
    val models = {
      val toximesh = new TriangleMesh
      toximesh.addFace(new Vec3D(0,0,1), new Vec3D(10,0,1), new Vec3D(0,10,1))
      val model = Mesh3DConverter(toximesh,"test")
      Array(model)
    }
    
    val segments:Array[IndexedSeq[ReadonlyVec3D]] = {
      val segment:Array[ReadonlyVec3D] = Array(new Vec3D(-20,-20,1), new Vec3D(20,20,1))
      Array(segment)
    }
    val jbc = new JBulletCollision(segments, models)
        
    val result = new MutableList[IndexedSeq[ReadonlyVec3D]]
    segments.foreach(segment => {
      segment.sliding(2,1).foreach(s => {
        val r = jbc.doRayTests(s)
        println("from: " + s(0) + " to:" + s(1) + " = " + r.mkString("\n   ","\n   ", ""))
        result += r
      })
    })
    println("Result: " + result)
    jbc.cleanup
  }
}
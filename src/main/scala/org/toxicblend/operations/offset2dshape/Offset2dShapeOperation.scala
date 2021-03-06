package org.toxicblend.operations.offset2dshape

import org.toxicblend.ToxicblendException
import org.toxicblend.UnitSystem
import org.toxicblend.util.Time.time
import org.toxicblend.CommandProcessorTrait
import org.toxicblend.geometry.ProjectionPlane.YZ_PLANE
import org.toxicblend.geometry.ProjectionPlane.XZ_PLANE
import org.toxicblend.geometry.ProjectionPlane.XY_PLANE
import org.toxicblend.typeconverters.Rings2DConverter
import org.toxicblend.util.Regex
import org.toxicblend.protobuf.ToxicBlendProtos.Message
import org.toxicblend.typeconverters.Mesh3DConverter
import org.toxicblend.typeconverters.OptionConverter
import org.toxicblend.typeconverters.Polygon2DConverter
import org.toxicblend.typeconverters.Matrix4x4Converter
import org.toxicblend.operations.boostmedianaxis.MedianAxisJni.simplify3D
import toxi.geom.Vec3D
import toxi.geom.Polygon2D
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

class Offset2dShapeOperation extends CommandProcessorTrait {
  
  def processInput(inMessage:Message, options:OptionConverter) = {
    val traceMsg = "Offset2dShapeOperation"
        
    val useMultiThreading = options.getMultiThreadingProperty(traceMsg,true)
    
    val useToOutline = options.getBooleanProperty("useToOutline", false, traceMsg)
    val unitScale = options.getUnitScaleProperty(traceMsg)
    val unitIsMetric = options.getUnitSystemProperty(traceMsg)
    val offset = options.getFloatProperty("offset", 0.1f, traceMsg)*unitScale/1000f // convert from meter to mm
     
    // Convert model vertices to world coordinates so that the offset unit makes sense
    val models = inMessage.getModelsList.map(inModel => {
      (Mesh3DConverter(inModel,true), // Unit is now [meter]
      if (inModel.hasWorldOrientation) {
        Option(Matrix4x4Converter(inModel.getWorldOrientation))
      } else {
        None
      })
    })
    
    val returnPolygons = time("FindPlanes calculation time: ", {
      def findSequenceOfPolygons( model:(Mesh3DConverter,Option[Matrix4x4Converter]) ) = {
        val segments = model._1.findContinuousLineSegments._2.filter(seq => seq.size>2)
        if (segments.size ==0) System.err.println(traceMsg + ": No edge sequence found in input model.")  
	      val pt = Polygon2DConverter.toPolygon2D(segments)
	      val name = model._1.name+ " offset " + options.getStringProperty("offset", "0.1") + "mm"
	      if (useToOutline) new Polygon2DConverter(pt.map(p => p._1.offsetShape(offset,toOutline=true)), pt.map(t => t._2), name)
	      else new Polygon2DConverter(pt.map(p => p._1.offsetShape(offset)), pt.map(t => t._2), name)
      }
      if (useMultiThreading) {
        models.par.map(model => findSequenceOfPolygons(model)).toSeq
      } else {
        models.map(model => findSequenceOfPolygons(model))
      }
    })
        
    time("Building resulting pBModel: ",{
      val returnMessageBuilder = Message.newBuilder
      returnPolygons.foreach(pc => returnMessageBuilder.addModels(pc.toPBModel(None)))
      returnMessageBuilder
    })
  }
}

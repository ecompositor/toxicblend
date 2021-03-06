#!/usr/bin/python   
import bpy
import toxicblend
import imp # needed when reloading toxicblend site-packages, won't be used in a release version

# How to install this plugin:
# 
# run this in the blender console:
#   import site; site.getsitepackages()
#
# copy the content of the toxicblend/src/main/blender_addon/site-packages directory to one of the 
# directories listed by the previous command. 
# 
# OSX example:
# cp -R toxicblend/src/main/blender_addon/site-packages/* /Applications/Blender-2.72b/blender-2.72b.app/Contents/MacOS/../Resources/2.72/python/lib/python3.4/site-packages
#
# then restart blender and use "Run script" on this file

bl_info = {
  "name": "Toxicblend - Simple gcode generator",
  'description': 'Naive implementation gcode generation, converts a set of edges into gcode.',
  'author': 'EAD Fritz',
  'blender': (2, 69, 0),
  "category": "Object",
}
          
class ToxicBlend_SimpleGcodeGenerator(bpy.types.Operator):
  '''Simple gcode generator'''
  bl_idname = "object.toxicblend_simplegcodegenerator"
  bl_label = "Toxicblend:Simple gcode generator"
  bl_options = {'REGISTER', 'UNDO'}  # enable undo for the operator.
  
  #useMultiThreadingProperty = bpy.props.EnumProperty(
  #  name="Use experimental mulithreading algorithm",
  #  items=(("TRUE", "True",""),
  #         ("FALSE", "False","")),
  #         #update=mode_update_callback
  #         default="FALSE"    
  #        )        
  #simplifyLimitProperty = bpy.props.FloatProperty(name="Simplify Limit", default=0.5, min=0.0001, max=100, description="the maximum allowed 3d deviation (in pixels) from a straight line, if the deviation is larger than this the line will be segmented.")  
  safeZProperty = bpy.props.FloatProperty(name="Safe Z[mm]", default=2, min=0.0001, max=100, description="Safe Z position, gcode will initiate at this height")
  stepdownProperty = bpy.props.FloatProperty(name="Stepdown Z[mm]", default=1000, min=0, max=2000, description="Maximum plunge depth per layer (set it large to disable layers)")
  g0FeedrateProperty = bpy.props.FloatProperty(name="G0 feedrate [mm/min]", default=1000, min=0, max=5000, description="Rapid feedrate")
  g1FeedrateProperty = bpy.props.FloatProperty(name="G1 feedrate [mm/min]", default=300, min=0, max=5000, description="Feedrate")
  g1PlungeFeedrateProperty = bpy.props.FloatProperty(name="plunge feedrate [mm/min]", default=301, min=0, max=5000, description="Plunge feedrate")
  spindleSpeedProperty = bpy.props.FloatProperty(name="Spindle speed [rpm]", default=5000, min=50, max=150000, description="Spingle speed")
  g64CommandProperty = bpy.props.StringProperty(name="Path blending command", default="G64 P0.02 Q0.02", description="Path blending command (G64). Leave empty if not applicable")
  customEndCommandProperty = bpy.props.StringProperty(name="Custom end command", default="M101", description="Custom end of run command (turn off coolant, notify operator, etc. etc). Leave empty if not applicable")
  filenameProperty = bpy.props.StringProperty(name="outFilename", default="gcode.ngc", description="the filename of the gcode file. Usually a .ngc file")
  
  @classmethod
  def poll(cls, context):
    return context.active_object is not None

  def execute(self, context):
    imp.reload(toxicblend) # needed when reloading toxicblend site-packages, won't be used in a release version
    try:
      with toxicblend.ByteCommunicator("localhost", 9999) as bc: 
        # bpy.context.selected_objects,
        activeObject = context.scene.objects.active
        unitSystemProperty = context.scene.unit_settings
      
        properties = {#'useMultiThreading'     : str(self.useMultiThreadingProperty),
                      #'simplifyLimit'         : str(self.simplifyLimitProperty),
                      'unitSystem'            : str(unitSystemProperty.system), 
                      'unitScale'             : str(unitSystemProperty.scale_length),
                      'safeZ'                 : str(self.safeZProperty),
                      'stepDown'              : self.stepdownProperty,
                      'g0Feedrate'            : str(self.g0FeedrateProperty),
                      'g1Feedrate'            : str(self.g1FeedrateProperty),
                      'g1PlungeFeedrate'      : str(self.g1PlungeFeedrateProperty),
                      'spindleSpeed'          : str(self.spindleSpeedProperty),
                      'g64Command'            : self.g64CommandProperty,
                      'customEndCommand'      : self.customEndCommandProperty,
                      'outFilename'           : self.filenameProperty}
                    
        bc.sendSingleBlenderObject(activeObject, self.bl_idname, properties) 
        bc.receiveObjects()
        return {'FINISHED'}
    except toxicblend.ToxicblendException as e:
      self.report({'ERROR'}, e.message)
      return {'CANCELLED'}
  
def register():
  # Check Blender version
  req = [2, 69, 0]
  (a,b,c) = bpy.app.version
  if a < req[0] or (a==req[0] and b<req[1]) or (a==req[0] and b == req[1] and c < req[2]):
    msg = 'Blender too old: %s < %s' % ((a,b,c), tuple(version))
    raise NameError(msg)
 
  bpy.utils.register_class(ToxicBlend_SimpleGcodeGenerator)

def unregister():
  bpy.utils.unregister_class(ToxicBlend_SimpleGcodeGenerator)

if __name__ == "__main__":
  register()
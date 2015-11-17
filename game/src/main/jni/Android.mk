LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE    := libgameengine
LOCAL_C_INCLUDES := 
 
LOCAL_CFLAGS := $(LOCAL_C_INCLUDES:%=-I%) -O2 -Wall -D__ANDROID__
LOCAL_CPPFLAGS := $(LOCAL_C_INCLUDES:%=-I%) -O2 -Wall -D__ANDROID__
LOCAL_LDLIBS := -lm -lGLESv2 -llog
LOCAL_ARM_MODE  := arm
 
LOCAL_SRC_FILES := Box2D/Collision/b2BroadPhase.cpp		\
	Box2D/Collision/b2CollideCircle.cpp		\
	Box2D/Collision/b2CollideEdge.cpp		\
	Box2D/Collision/b2CollidePolygon.cpp		\
	Box2D/Collision/b2Collision.cpp		\
	Box2D/Collision/b2Distance.cpp		\
	Box2D/Collision/b2DynamicTree.cpp		\
	Box2D/Collision/b2TimeOfImpact.cpp		\
	Box2D/Collision/Shapes/b2ChainShape.cpp		\
	Box2D/Collision/Shapes/b2CircleShape.cpp		\
	Box2D/Collision/Shapes/b2EdgeShape.cpp		\
	Box2D/Collision/Shapes/b2PolygonShape.cpp		\
	Box2D/Common/b2BlockAllocator.cpp		\
	Box2D/Common/b2Draw.cpp		\
	Box2D/Common/b2Math.cpp		\
	Box2D/Common/b2Settings.cpp		\
	Box2D/Common/b2StackAllocator.cpp		\
	Box2D/Common/b2Timer.cpp		\
	Box2D/Dynamics/b2Body.cpp		\
	Box2D/Dynamics/b2ContactManager.cpp		\
	Box2D/Dynamics/b2Fixture.cpp		\
	Box2D/Dynamics/b2Island.cpp		\
	Box2D/Dynamics/b2World.cpp		\
	Box2D/Dynamics/b2WorldCallbacks.cpp		\
	Box2D/Dynamics/Contacts/b2ChainAndCircleContact.cpp		\
	Box2D/Dynamics/Contacts/b2ChainAndPolygonContact.cpp		\
	Box2D/Dynamics/Contacts/b2CircleContact.cpp		\
	Box2D/Dynamics/Contacts/b2Contact.cpp		\
	Box2D/Dynamics/Contacts/b2ContactSolver.cpp		\
	Box2D/Dynamics/Contacts/b2EdgeAndCircleContact.cpp		\
	Box2D/Dynamics/Contacts/b2EdgeAndPolygonContact.cpp		\
	Box2D/Dynamics/Contacts/b2PolygonAndCircleContact.cpp		\
	Box2D/Dynamics/Contacts/b2PolygonContact.cpp		\
	Box2D/Dynamics/Joints/b2DistanceJoint.cpp		\
	Box2D/Dynamics/Joints/b2FrictionJoint.cpp		\
	Box2D/Dynamics/Joints/b2GearJoint.cpp		\
	Box2D/Dynamics/Joints/b2Joint.cpp		\
	Box2D/Dynamics/Joints/b2MotorJoint.cpp		\
	Box2D/Dynamics/Joints/b2MouseJoint.cpp		\
	Box2D/Dynamics/Joints/b2PrismaticJoint.cpp		\
	Box2D/Dynamics/Joints/b2PulleyJoint.cpp		\
	Box2D/Dynamics/Joints/b2RevoluteJoint.cpp		\
	Box2D/Dynamics/Joints/b2RopeJoint.cpp		\
	Box2D/Dynamics/Joints/b2WeldJoint.cpp		\
	Box2D/Dynamics/Joints/b2WheelJoint.cpp		\
	Box2D/Rope/b2Rope.cpp		\
	Wrapper/AndroidGL20.cpp		\
	Wrapper/Box2D/Collision/Shape/ChainShape.cpp		\
	Wrapper/Box2D/Collision/Shape/CircleShape.cpp		\
	Wrapper/Box2D/Collision/Shape/EdgeShape.cpp		\
	Wrapper/Box2D/Collision/Shape/PolygonShape.cpp		\
	Wrapper/Box2D/Collision/Shape/Shape.cpp		\
	Wrapper/Box2D/Common/BufferUtils.cpp		\
	Wrapper/Box2D/Common/ETC1.cpp		\
	Wrapper/Box2D/Common/ETC1Utils.cpp		\
	Wrapper/Box2D/Common/2DPixmap.cpp		\
	Wrapper/Box2D/Common/JPGD.cpp		\
	Wrapper/Box2D/Common/JPGDC.cpp		\
	Wrapper/Box2D/Common/Matrix4.cpp		\
	Wrapper/Box2D/Common/Stb_Image.cpp		\
	Wrapper/Box2D/Common/Version.cpp		\
	Wrapper/Box2D/Dynamics/Body.cpp		\
	Wrapper/Box2D/Dynamics/Contacts/Contact.cpp		\
	Wrapper/Box2D/Dynamics/Contacts/ContactImpulse.cpp		\
	Wrapper/Box2D/Dynamics/Fixture.cpp		\
	Wrapper/Box2D/Dynamics/Joints/DistanceJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/FrictionJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/GearJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/Joint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/MotorJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/MouseJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/PrismaticJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/PulleyJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/RevoluteJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/RopeJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/WeldJoint.cpp		\
	Wrapper/Box2D/Dynamics/Joints/WheelJoint.cpp		\
	Wrapper/Box2D/Dynamics/Manifold.cpp		\
	Wrapper/Box2D/Dynamics/World.cpp		\
	memcpy_wrap.c
 
include $(BUILD_SHARED_LIBRARY)

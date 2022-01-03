# polytechWebotsController

This is a repo used for teaching. It provides a java based API to control a cyberbotics robots (webots: https://cyberbotics.com/) based on the "create" robot (https://cyberbotics.com/doc/guide/create) augmented with:
  * a pen
  * a gripper
  * a front camera
  * a back camera
  * a front distance sensor
  * a front left distance sensor
  * a front right distance sensor

For instance in the TFSM course, this is the basic API used to define the control by using a state chart defined by using Yakindu (https://www.itemis.com/en/yakindu/state-machine/). In the GEMOC options, it has been used to define a dedicated language (syntax), its operational semantics and the associated debugger, based on the GEMOC studio (gemoc.org/studio.html).


In order to put the webots library in the classpath, one should do
`export LD_LIBRARY_PATH=/snap/webots/current/usr/share/webots/lib/controller/` in the launching terminal with the appropriate path before to launch eclipse.

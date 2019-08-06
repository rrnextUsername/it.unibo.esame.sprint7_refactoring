%====================================================================================
% butler description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxbutler, "localhost",  "MQTT", "0" ).
context(ctxrobotmind, "localhost",  "MQTT", "0" ).
 qactor( resourcemodel, ctxrobotmind, "external").
  qactor( onestepahead, ctxrobotmind, "external").
  qactor( butler_solver, ctxbutler, "it.unibo.butler_solver.Butler_solver").
  qactor( butler_pathfinder_handler, ctxbutler, "it.unibo.butler_pathfinder_handler.Butler_pathfinder_handler").
  qactor( butler_fridge_handler, ctxbutler, "it.unibo.butler_fridge_handler.Butler_fridge_handler").
  qactor( butler_test_handler, ctxbutler, "it.unibo.butler_test_handler.Butler_test_handler").
  qactor( butler_router, ctxbutler, "it.unibo.butler_router.Butler_router").
  qactor( pathfinder, ctxbutler, "it.unibo.pathfinder.Pathfinder").
  qactor( dummy_obstacle, ctxbutler, "it.unibo.dummy_obstacle.Dummy_obstacle").
  qactor( frontend_dummy, ctxbutler, "it.unibo.frontend_dummy.Frontend_dummy").
  qactor( fridge_cmd_solver, ctxbutler, "it.unibo.fridge_cmd_solver.Fridge_cmd_solver").
  qactor( fridge_model_handler, ctxbutler, "it.unibo.fridge_model_handler.Fridge_model_handler").

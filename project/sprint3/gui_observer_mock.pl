%====================================================================================
% gui_observer_mock description   
%====================================================================================
%====================================================================================
context(ctxwis24, "127.0.0.1",  "TCP", "8121").
context(ctxgui_observer_mock, "localhost",  "TCP", "8124").
 qactor( gui_observer, ctxgui_observer_mock, "it.unibo.gui_observer.Gui_observer").
 static(gui_observer).

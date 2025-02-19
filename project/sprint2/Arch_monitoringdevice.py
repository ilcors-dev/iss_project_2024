### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('monitoringdeviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxmonitoringdevice', graph_attr=nodeattr):
          led=Custom('led','./qakicons/symActorSmall.png')
          led_device=Custom('led_device','./qakicons/symActorSmall.png')
          sonar=Custom('sonar','./qakicons/symActorSmall.png')
          sonar_device=Custom('sonar_device','./qakicons/symActorSmall.png')
     with Cluster('ctxwis24', graph_attr=nodeattr):
          wis=Custom('wis(ext)','./qakicons/externalQActor.png')
     sonar_device >> Edge( label='sonar_data', **eventedgeattr, decorate='true', fontcolor='red') >> sonar
     sonar_device >> Edge(color='blue', style='solid',  decorate='true', label='<sonar_sensitivity &nbsp; >',  fontcolor='blue') >> sonar
     led >> Edge(color='blue', style='solid',  decorate='true', label='<update_physical_led_mode &nbsp; >',  fontcolor='blue') >> led_device
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<ash_measurement &nbsp; >',  fontcolor='blue') >> wis
diag

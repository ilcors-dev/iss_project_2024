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
with Diagram('wis24Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxwis24_functional_test', graph_attr=nodeattr):
          wis=Custom('wis','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorSmall.png')
          scale=Custom('scale','./qakicons/symActorSmall.png')
          scale_device=Custom('scale_device','./qakicons/symActorSmall.png')
          led=Custom('led','./qakicons/symActorSmall.png')
          led_device=Custom('led_device','./qakicons/symActorSmall.png')
          sonar=Custom('sonar','./qakicons/symActorSmall.png')
          sonar_device=Custom('sonar_device','./qakicons/symActorSmall.png')
     scale_device >> Edge( label='scale_data', **eventedgeattr, decorate='true', fontcolor='red') >> scale
     sonar_device >> Edge( label='sonar_data', **eventedgeattr, decorate='true', fontcolor='red') >> sonar
     oprobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     wis >> Edge(color='magenta', style='solid', decorate='true', label='<getrp<font color="darkgreen"> getrp_status</font> &nbsp; depositrp<font color="darkgreen"> depositrp_status</font> &nbsp; depositash<font color="darkgreen"> depositash_status</font> &nbsp; gohome<font color="darkgreen"> gohome_status</font> &nbsp; extractash<font color="darkgreen"> extractash_status</font> &nbsp; >',  fontcolor='magenta') >> oprobot
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<load_ash &nbsp; >',  fontcolor='blue') >> sonar_device
     incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<burning &nbsp; finishedBurning &nbsp; >',  fontcolor='blue') >> wis
     led >> Edge(color='blue', style='solid',  decorate='true', label='<update_physical_led_mode &nbsp; >',  fontcolor='blue') >> led_device
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<unload_weight &nbsp; >',  fontcolor='blue') >> scale_device
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<update_led_mode &nbsp; >',  fontcolor='blue') >> led
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<startIncinerator &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<ash_measurement &nbsp; >',  fontcolor='blue') >> wis
     scale >> Edge(color='blue', style='solid',  decorate='true', label='<update_scale_count &nbsp; >',  fontcolor='blue') >> wis
diag

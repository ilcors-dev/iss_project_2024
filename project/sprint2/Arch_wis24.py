### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
    'margin': '0',  # Remove margins around the diagram
    'pad': '0.2',   # Reduce padding
    'dpi': '300'    # Increase resolution for better quality
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
     with Cluster('ctxwis24', graph_attr=nodeattr):
          wis=Custom('wis','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorSmall.png')
     with Cluster('ctxscale', graph_attr=nodeattr):
          scale_device=Custom('scale_device(ext)','./qakicons/externalQActor.png')
     oprobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     wis >> Edge(color='magenta', style='solid', decorate='true', label='<getrp<font color="darkgreen"> getrp_status</font> &nbsp; depositrp<font color="darkgreen"> depositrp_status</font> &nbsp; depositash<font color="darkgreen"> depositash_status</font> &nbsp; gohome<font color="darkgreen"> gohome_status</font> &nbsp; extractash<font color="darkgreen"> extractash_status</font> &nbsp; >',  fontcolor='magenta') >> oprobot
     incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<burning &nbsp; finishedBurning &nbsp; >',  fontcolor='blue') >> wis
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<unload_weight &nbsp; >',  fontcolor='blue') >> scale_device
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<startIncinerator &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
diag

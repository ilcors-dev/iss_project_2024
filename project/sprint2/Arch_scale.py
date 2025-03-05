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
with Diagram('scaleArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxwis24', graph_attr=nodeattr):
          wis=Custom('wis(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxscale', graph_attr=nodeattr):
          scale=Custom('scale','./qakicons/symActorSmall.png')
          scale_device=Custom('scale_device','./qakicons/symActorSmall.png')
     scale_device >> Edge( label='scale_data', **eventedgeattr, decorate='true', fontcolor='red') >> scale
     scale >> Edge(color='blue', style='solid',  decorate='true', label='<update_scale_count &nbsp; >',  fontcolor='blue') >> wis
diag

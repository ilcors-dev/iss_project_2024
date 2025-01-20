from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + '/usr/local/bin'

graphattr = {
    'fontsize': '22',
    'splines': 'ortho',  # Ensures edges are orthogonal (90-degree angles)
    'nodesep': '1.0',    # Increases space between nodes
    'ranksep': '1.5',    # Increases vertical space between clusters
}

nodeattr = {
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted',
    'fontsize': '18',
    'labeldistance': '2.0'  # Increases distance of the label from the line
}

evattr = {
    'color': 'darkgreen',
    'style': 'dotted',
    'fontsize': '18',
    'labeldistance': '2.0'
}

with Diagram('wis24Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
    with Cluster('env'):
        sys = Custom('System', './qakicons/system.png')
        with Cluster('ctxwis24', graph_attr=nodeattr):
            wis = Custom('wis', './qakicons/symActorSmall.png')
            oprobot = Custom('oprobot', './qakicons/symActorSmall.png')
            incinerator = Custom('incinerator', './qakicons/symActorSmall.png')
        with Cluster('ctxbasicrobot', graph_attr=nodeattr):
            basicrobot = Custom('basicrobot(ext)', './qakicons/externalQActor.png')
        with Cluster('ctxmonitoringdevice', graph_attr=nodeattr):
            monitoringdevice = Custom('monitoringdevice', './qakicons/symActorSmall.png')

        sys >> Edge(label='burning', **evattr, fontcolor='darkgreen') >> wis
        sys >> Edge(label='finishedBurning', **evattr, fontcolor='darkgreen') >> wis
        incinerator >> Edge(label='burning', **eventedgeattr, fontcolor='red') >> sys
        incinerator >> Edge(label='finishedBurning', **eventedgeattr, fontcolor='red') >> sys
        monitoringdevice >> Edge(color='blue', style='solid', decorate='true',
                                 label='<ashMeasurement &nbsp; >', fontcolor='blue') >> wis

diag


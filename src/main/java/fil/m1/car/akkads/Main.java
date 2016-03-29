package fil.m1.car.akkads;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import fil.m1.car.akkads.actor.HistoryKeeperActor;
import fil.m1.car.akkads.actor.NodeDataSendingActor;
import fil.m1.car.akkads.message.DataMessage;
import fil.m1.car.akkads.message.RenderHistoryMessage;
import fil.m1.car.akkads.message.SetHierarchyMessage;

public class Main {

    public static void main(String[] args) throws Exception {
        final String configFile1 = Main.class.getClassLoader().getResource("conf1.conf").getFile();
        final String configFile2 = Main.class.getClassLoader().getResource("conf2.conf").getFile();
        final String configFile3 = Main.class.getClassLoader().getResource("conf3.conf").getFile();
        
        final Config config1 = ConfigFactory.parseFile(new File(configFile1));
        final Config config2 = ConfigFactory.parseFile(new File(configFile2));
        final Config config3 = ConfigFactory.parseFile(new File(configFile3));
        
        final ActorSystem actorSystem1 = ActorSystem.create("JediDataSendingSystem1", config1);
        final ActorSystem actorSystem2 = ActorSystem.create("JediDataSendingSystem2", config2);
        final ActorSystem actorSystem3 = ActorSystem.create("HistorySystem", config3);
        
        final ActorRef node1 = actorSystem1.actorOf(Props.create(NodeDataSendingActor.class), "Node1");
        final ActorRef node2 = actorSystem2.actorOf(Props.create(NodeDataSendingActor.class), "Node2");
        final ActorRef node3 = actorSystem1.actorOf(Props.create(NodeDataSendingActor.class), "Node3");
        final ActorRef node4 = actorSystem2.actorOf(Props.create(NodeDataSendingActor.class), "Node4");
        final ActorRef node5 = actorSystem1.actorOf(Props.create(NodeDataSendingActor.class), "Node5");
        final ActorRef node6 = actorSystem2.actorOf(Props.create(NodeDataSendingActor.class), "Node6");
        
        final ActorRef historyKeeper = actorSystem3.actorOf(Props.create(HistoryKeeperActor.class), "history");
        
        node1.tell(new SetHierarchyMessage(null, Arrays.asList(node2, node5)), ActorRef.noSender());
        node2.tell(new SetHierarchyMessage(node1, Arrays.asList(node3, node4)), ActorRef.noSender());
        node5.tell(new SetHierarchyMessage(node1, Arrays.asList(node6)), ActorRef.noSender());
        node3.tell(new SetHierarchyMessage(node2, new LinkedList<ActorRef>()), ActorRef.noSender());
        node4.tell(new SetHierarchyMessage(node2, new LinkedList<ActorRef>()), ActorRef.noSender());
        node6.tell(new SetHierarchyMessage(null, new LinkedList<ActorRef>()), ActorRef.noSender());

        node2.tell(new DataMessage("Keyser S�ze"), ActorRef.noSender());
        
        // TODO has to be removed
        Thread.sleep(10000);
        
        historyKeeper.tell(new RenderHistoryMessage(), ActorRef.noSender());
        
        /*final String configFile = Main2.class.getClassLoader().getResource("conf2.conf").getFile();
        final Config config = ConfigFactory.parseFile(new File(configFile));
        final ActorSystem system = ActorSystem.create("ClientSystem", config);
        final ActorRef localActor = system.actorOf(Props.create(NodeDataSendingActor.class), "Node1");*/
    }


}

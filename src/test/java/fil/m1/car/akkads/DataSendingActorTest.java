package fil.m1.car.akkads;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import fil.m1.car.akkads.actor.TreeNodeDataSendingActor;

public class DataSendingActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Ignore
    public void testOnReceive() throws Exception {

        new JavaTestKit(system) {{
            final Props props = Props.create(TreeNodeDataSendingActor.class);
            final ActorRef subject = system.actorOf(props);
            subject.tell("Hello", ActorRef.noSender());
            expectMsgEquals("Hello");
        }};
    }
}
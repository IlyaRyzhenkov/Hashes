package tests;

import Algorithms.Rendezvous;
import org.junit.Test;
import utils.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RendezvousTest {
    @Test
    public void testNodeCounter() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        assertEquals(1, hasher.getNumberOfNodes());

        Node<Integer> n2 = new Node<>("n2");
        hasher.addNode("n2", n2);
        assertEquals(2, hasher.getNumberOfNodes());

        hasher.removeNode("n1");
        assertEquals(1, hasher.getNumberOfNodes());

        hasher.removeNode("n2");
        assertEquals(0, hasher.getNumberOfNodes());
    }

    @Test
    public void testRemoveFromEmpty() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        hasher.removeNode("0");
    }

    @Test
    public void testRemoveFromEmpty2() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        hasher.addKey("1", 1);
        hasher.removeNode("n1");
        assertNull(hasher.getKey("1"));
    }

    @Test
    public void testRemoveWrongNode() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        hasher.removeNode("n2");
        assertEquals(1, hasher.getNumberOfNodes());
    }

    @Test
    public void testKeyStorage() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        Node<Integer> n2 = new Node<>("n2");
        hasher.addNode("n2", n2);
        Node<Integer> n3 = new Node<>("n3");
        hasher.addNode("n3", n3);

        hasher.addKey("1", 1);
        int v = hasher.getKey("1");
        assertEquals(1, v);

        hasher.addKey("2", 2);
        v = hasher.getKey("2");
        assertEquals(2, v);
    }

    @Test
    public void testAddNode() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);

        hasher.addKey("1", 1);
        hasher.addKey("2", 2);

        Node<Integer> n2 = new Node<>("n2");
        hasher.addNode("n2", n2);

        hasher.addKey("3", 3);

        Node<Integer> n3 = new Node<>("n3");
        hasher.addNode("n3", n3);

        hasher.addKey("4", 4);
        assertEquals((Integer) 1, hasher.getKey("1"));
        assertEquals((Integer) 2, hasher.getKey("2"));
        assertEquals((Integer) 3, hasher.getKey("3"));
        assertEquals((Integer) 4, hasher.getKey("4"));
    }

    @Test
    public void testRemoveNode() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        Node<Integer> n2 = new Node<>("n2");
        hasher.addNode("n2", n2);
        Node<Integer> n3 = new Node<>("n3");
        hasher.addNode("n3", n3);

        hasher.addKey("1", 1);
        hasher.addKey("2", 2);
        hasher.removeNode("n1");
        hasher.addKey("3", 3);
        hasher.removeNode("n2");
        hasher.addKey("4", 4);

        assertEquals((Integer) 1, hasher.getKey("1"));
        assertEquals((Integer) 2, hasher.getKey("2"));
        assertEquals((Integer) 3, hasher.getKey("3"));
        assertEquals((Integer) 4, hasher.getKey("4"));
    }

    @Test
    public void testMixed() {
        Rendezvous<Integer> hasher = new Rendezvous<>();
        Node<Integer> n1 = new Node<>("n1");
        hasher.addNode("n1", n1);
        hasher.addKey("4", 4);
        Node<Integer> n2 = new Node<>("n2");
        hasher.addNode("n2", n2);
        hasher.addKey("3", 3);
        hasher.removeNode("n1");
        Node<Integer> n3 = new Node<>("n3");
        hasher.addNode("n3", n3);
        hasher.addKey("2", 2);
        Node<Integer> n4 = new Node<>("n4");
        hasher.addNode("n4", n4);
        hasher.addKey("1", 1);

        assertEquals((Integer) 1, hasher.getKey("1"));
        assertEquals((Integer) 2, hasher.getKey("2"));
        assertEquals((Integer) 3, hasher.getKey("3"));
        assertEquals((Integer) 4, hasher.getKey("4"));
    }
}

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// The Node class works with the CustomQueue class to provide linked list functionality of storing some kind of data and a pointer to the next Node
public class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public static void doUnitTests() {
        System.out.println("Running Node tests");

        // Create a log file for test results
        try {
            File file = new File("Node Test Results.csv");
            FileWriter writer = new FileWriter(file);

            // Test 1: Node creation and getData
            Node<String> node1 = new Node<>("test1");
            writer.write("Test 1: Node creation and getData, " + "test1".equals(node1.getData()) + "\n");

            // Test 2: setData
            node1.setData("test2");
            writer.write("Test 2: setData, " + "test2".equals(node1.getData()) + "\n");

            // Test 3: setNext and getNext
            Node<String> node2 = new Node<>("test3");
            node1.setNext(node2);
            writer.write("Test 3: setNext and getNext, " + (node1.getNext() == node2) + "\n");

            writer.close();
            System.out.println("File created at " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


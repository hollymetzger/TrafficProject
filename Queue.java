import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Provides linked list structure to model a queue
public class Queue<T> {
    private Node<T> head;
    private Node<T> tail;

    public Queue() {
        head = null;
        tail = null;
    }

    public void enqueue(T obj) {
        Node<T> newNode = new Node<>(obj);
        if (head == null) {
            head = newNode;
            tail = head;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    public T dequeue() {
        if (head == null) {
            return null;
        } else {
            Node<T> temp = head;
            head = head.getNext();
            return temp.getData();
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Node<T> getHead() {
        return head;
    }

    public static void doUnitTests() {
        System.out.println("Running Queue tests");

        try {
            File file = new File("Queue Test Results.csv");
            FileWriter writer = new FileWriter(file);

            // Test 1: Queue initialization
            Queue<String> queue = new Queue<>();
            writer.write("Test 1: Queue initialization, " + queue.isEmpty() + "\n");

            // Test 2: Enqueue operation
            queue.enqueue("item1");
            writer.write("Test 2: Enqueue operation, " + ("item1".equals(queue.getHead().getData())) + "\n");

            // Test 3: Enqueue multiple items
            queue.enqueue("item2");
            writer.write("Test 3: Enqueue multiple items, " + ("item2".equals(queue.getHead().getNext().getData())) + "\n");

            // Test 4: Dequeue operation
            String dequeuedItem = queue.dequeue();
            writer.write("Test 4: Dequeue operation, " + ("item1".equals(dequeuedItem)) + "\n");

            // Test 5: Dequeue until empty
            queue.dequeue();
            writer.write("Test 5: Dequeue until empty, " + queue.isEmpty() + "\n");

            writer.close();
            System.out.println("File created at " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

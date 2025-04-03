public class UnitTest {
    public static void main(String[] args) {
        System.out.println("Running Unit Tests");

        Location.doUnitTests();
        Person.doUnitTests();
        People.doUnitTests();
        Vehicle.doUnitTests();
    }
}

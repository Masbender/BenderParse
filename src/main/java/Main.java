import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class Main {
  public static void main(String[] args) 
	throws IOException {
		Scanner sc = new Scanner(System.in);
		Session s = new Session("src/main/java/ch1.txt", "1");
		s.addIfFunc("sqrt", (String in) -> {return Integer.valueOf(in) < 0 ? null : Double.toString(Math.sqrt(Integer.valueOf(in)));});
		s.update();
		for (int i = 0; i < 3; i++)
		{
			System.out.println(s.getOutput());
			Set<String> options = s.getInputOptions();
			for (String option : options) {
				System.out.println("Option: " + option);
			}
			
			while (true) {
				System.out.print("Input: ");
				String input = sc.nextLine();

				for (String option : options) {
					if (input.equals(option)) {
						s.giveInput(option);
					}
				}

				if (options.contains(input)) {
					break;
				}
			}
		}
		sc.close();
  }

  // @Test
  // void addition() {
  //     assertEquals(2, 1 + 1);
  // }
}
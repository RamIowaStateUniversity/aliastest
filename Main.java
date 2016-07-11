public class Main {
	private void alias() {
		int a = b;
		int c = 1;
		int d = e;
		a = 5;
		d = c;
	}

	private void alias_branch() {
		int a = b;
		a = 5;
		if(a==5) {
			a = 6;
		}
		else {
			a = 7;
		}
		d = c;
		int c = 1;
		int d = e;
	}

	private void alias_loop() {
		int a = b;
		a = 5;
		if(a==5) {
			a = 6;
		}
		else {
			a = 7;
		}
		d = c;
		int c = 1;
		while(true) {
			int x =6;
			int y = 7;
		}
		System.out.println(a);
		int d = e;
	}
}

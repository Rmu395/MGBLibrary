package library.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	/*
	* 1/4pkt: CRUD + Spring Security z podziałem na role : admin -dodaje i usuwa  ksiazki z ksiegarni, user możne przeglądać.
	* 1/4pkt: CRUD + koszyk - dodawanie usuwanie książek przez Usera.
	* 1/4pkt: Zamówienia -  Dodanie funkcji które może wykonywać User - składanie zamówień na książki, zmiana statusu zamówienia przez admina.
	* 1/4pkt: Płatności - podpięcie np. Stripe
	*/

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

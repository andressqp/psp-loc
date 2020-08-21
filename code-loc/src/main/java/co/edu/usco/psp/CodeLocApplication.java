package co.edu.usco.psp;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.usco.psp.service.MetricaService;

@SpringBootApplication
public class CodeLocApplication implements CommandLineRunner {
	
	@Autowired
	private MetricaService metricaService;

	public static void main(String[] args) {
		SpringApplication.run(CodeLocApplication.class, args);
	}
	
	
	@Override
	public void run(String... args) throws IOException {

		Scanner lectorTeclado = new Scanner(System.in);
		
		System.out.println("--------- Digite la ruta de ubicación del proyecto ---------");
		System.out.println("--------- Unicamente se aceptan proyectos Java ---------");
		
		String rutaProyecto = lectorTeclado.next();
		
		this.metricaService.calcularMetricas(rutaProyecto);
		
		lectorTeclado.close();
	}
	
}

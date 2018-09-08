package com.lakj.comspace.pgp.encryption;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.PGPDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * This app encrypt a given string text message and save it to a file.
 * 
 * Created by Lak J Comspace on 8/9/2018.
 *
 */
public class App {

	public static void main(String[] args) throws Exception {

		final String mySecretMessage = "This is a top secret.";
		final String outputDirPath = "output";
		final String encryptedFileName = "mySecretFile.txt";

		File outputDir = new File(outputDirPath);
		outputDir.mkdirs(); // Create output directory if does not exist.

		System.out.println("Start encryption...");

		CamelContext camelContext = new DefaultCamelContext();

		camelContext.addRoutes(new RouteBuilder() {

			public void configure() throws Exception {

				PGPDataFormat format = new PGPDataFormat();

				format.setKeyFileName("file:keys/public.gpg");
				format.setKeyUserid("pgpencrypt@lakjcomspace.com");
				format.setArmored(true);

				from("direct:textContent").marshal(format)
						.to("file:" + outputDirPath + "/?fileName=" + encryptedFileName + "&charset=utf-8");

			}
		});

		camelContext.start();

		ProducerTemplate template = camelContext.createProducerTemplate();
		template.sendBody("direct:textContent", mySecretMessage);

		camelContext.stop();

		System.out.println("Encrypted file was saved.");
	}

}

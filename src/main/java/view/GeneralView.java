package view;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import controller.AccountHolderController;
import controller.TransactionController;
import model.Account;
import model.AccountHolder;
import model.Transaction;
import model.types.AccountType;

public class GeneralView {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int option = 0;

		AccountHolderController ahc = new AccountHolderController();

		do {
			System.out.println("Olá! Bem vindo ao YouBank :). O que deseja?");
			System.out.println("1. Realizar Login\n2. Realizar Cadastro");
			option = scan.nextInt();

			switch (option) {
				case 1:
					break;
				case 2:
					AccountHolder accountHolder = new AccountHolder();
					System.out.println("Certo, primeiro me diga o seu nome completo: ");
					accountHolder.setName(scan.nextLine());
					System.out.println("Agora me informe o seu CPF (pode ser com ponto e traço): ");
					accountHolder.setCpf(scan.nextLine());

					System.out.println();

					Account account = new Account();
					account.setAccountHolder(accountHolder);
					account.setOpenDate(new Date());
					account.setActive(true);

					System.out.println("Qual tipo de conta deseja criar?\n 1. Corrente\n 2. Poupança");
					int choice = scan.nextInt();
					if (choice == 1) account.setType(AccountType.CURRENT_ACCOUNT);
					else if (choice == 2) account.setType(AccountType.SAVINGS_ACCOUNT);

					System.out.println();

					scan.nextLine();
					
					try {
						System.out.println("Quase tudo pronto! Agora informe uma senha para acessar sua conta posteriormente: ");
						byte password[] = MessageDigest.getInstance("MD5").digest(scan.nextLine().getBytes("UTF-8"));
						account.setPassword(Base64.getEncoder().encodeToString(password));
					} catch (Exception e) {
						System.out.println("Algum erro aconteceu! Não foi possível criar a conta");
						break;
					}

					ahc.store(accountHolder);
					System.out.println("Conta criada com sucesso!");
					break;
				case 3:
					break;
				default:
					System.out.println("Não tem essa opção >:(");
					break;
			}
		} while (option != 0);
	}

}

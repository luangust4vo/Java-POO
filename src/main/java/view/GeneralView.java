package view;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import controller.AccountController;
import controller.AccountHolderController;
import controller.TransactionController;
import model.Account;
import model.AccountHolder;
import model.Transaction;
import model.types.AccountType;

public class GeneralView {
	private static final Scanner scan = new Scanner(System.in);
	private static final AccountHolderController ahc = new AccountHolderController();
	private static final AccountController ac = new AccountController();

	public static void main(String[] args) {
		int option;

		loop: while(true) {
			System.out.println("Olá! Bem vindo ao YouBank :). Já é um cliente nosso?");
			System.out.println("1. SIM :)\n2. NÃO :(\n3. SAIR :(");

			option = scan.nextInt();
			scan.nextLine();

			if (option == 1) {
				showMenu();
			} else if (option == 2) {
				if (registerNewAccountHolder()) {
					continue;
				}

				System.out.println("Algum problema ocorreu ao tentar cadastrar um novo usuário :(");
				return;
			} else if (option == 3) break loop;
			else {
				System.out.println("Não tem essa opção >:(");
				break;
			}
		}
	}

	private static void showMenu() {
		int option;

		while(true) {
			System.out.println("Que bom! Então me diga o que deseja fazer:");
			System.out.println("1. Acessar minha conta\n2. Criar uma nova conta\n3. Sair");

			option = scan.nextInt();
			scan.nextLine();

			switch (option) {
				case 1:
					if (loginIntoAccount()) {
						System.out.println("logado");
						// Algum método para fazer transação, ver extrato, etc
					}

					break;
				case 2:
					if (registerNewAccount()) {
						System.out.println("logado");
						// Algum método para fazer transação, ver extrato, etc
					}

					break;
				case 3:
					break;
				default:
					System.out.println("Não tem essa opção >:(");
					break;
			}
		}
	}

	private static boolean registerNewAccountHolder() {
		System.out.println("Deseja criar um usuário para acessar o YouBank? (S/N)");
		if (scan.nextLine().equalsIgnoreCase("S")) {
			System.out.println("Perfeito! Então vamos começar com o seu nome: ");
			AccountHolder accountHolder = new AccountHolder();
			accountHolder.setName(scan.nextLine());
			System.out.println("Agora me informe o seu CPF (pode ser com ponto e traço): ");
			accountHolder.setCpf(scan.nextLine());
			ahc.store(accountHolder);
			System.out.println("Usuário criado com sucesso!");
			return true;
		} else {
			return false;
		}
	}

	private static boolean loginIntoAccount() {
		System.out.println("Certo! Então me diga o seu CPF:");
		String cpf = scan.nextLine();

		AccountHolder accountHolder = ahc.findByCpf(cpf);
		if (accountHolder == null) {
			System.out.println("Não encontramos nenhum usuário com esse CPF :(");
			return false;
		}

		List<Account> accounts = ac.getAccountsByCpf(cpf);
		if (accounts.isEmpty()) {
			System.out.println("Não encontramos nenhuma conta associada a esse CPF :(");
			return false;
		}

		System.out.println("Escolha a conta que deseja acessar: ");
		for (int i = 0; i < accounts.size(); i++) {
			System.out.println((i + 1) + ". " + accounts.get(i).getName());
		}

		int choice = scan.nextInt() - 1;
		scan.nextLine();
		Account account = accounts.get(choice);

		System.out.println("Agora me informe a senha da conta: ");
		String password = scan.nextLine();

		try {
			String hashedPassword = Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(password.getBytes("UTF-8")));

			if (account.getPassword().equals(hashedPassword)) {
				System.out.println("Acesso permitido! Seja bem vindo " + accountHolder.getName());
				return true;
			} else {
				System.out.println(account.getPassword());
				System.out.println(hashedPassword);
				System.out.println("Senha incorreta! Tente novamente.");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Algum erro aconteceu! Não foi possível acessar a conta");
			return false;
		}
	}

	private static boolean registerNewAccount() {
		System.out.println("Certo! Então me diga o seu CPF:");
		String cpf = scan.nextLine();

		AccountHolder accountHolder = ahc.findByCpf(cpf);
		if (accountHolder == null) {
			System.out.println("Não encontramos nenhum usuário com esse CPF :(");
			return false;
		}

		Account account = new Account();
		account.setAccountHolder(accountHolder);
		account.setOpenDate(new Date());
		account.setActive(true);

		System.out.println("Qual tipo de conta deseja criar?\n 1. Corrente\n 2. Poupança");
		int choice = scan.nextInt();
		scan.nextLine();
		if (choice == 1) account.setType(AccountType.CURRENT_ACCOUNT);
		else if (choice == 2) account.setType(AccountType.SAVINGS_ACCOUNT);

		System.out.println("Agora dê um nome para a conta:");
		account.setName(scan.nextLine());	

		System.out.println();

		try {
			System.out.println("Quase tudo pronto! Agora informe uma senha para acessar sua conta posteriormente: ");
			byte password[] = MessageDigest.getInstance("MD5").digest(scan.nextLine().getBytes("UTF-8"));
			account.setPassword(Base64.getEncoder().encodeToString(password));
		} catch (Exception e) {
			System.out.println("Algum erro aconteceu! Não foi possível criar a conta");
			return false;
		}

		ac.store(account);
		System.out.println("Conta criada com sucesso!");
		return true;
	}
}

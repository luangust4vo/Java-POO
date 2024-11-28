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
import model.types.TransactionType;

public class GeneralView {
	private static final Scanner scan = new Scanner(System.in);
	private static final AccountHolderController ahc = new AccountHolderController();
	private static final AccountController ac = new AccountController();
	private static final TransactionController tc = new TransactionController();

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

		loop: while(true) {
			System.out.println("Que bom! Então me diga o que deseja fazer:");
			System.out.println("1. Acessar minha conta\n2. Criar uma nova conta\n3. Sair");

			option = scan.nextInt();
			scan.nextLine();

			Account account = null;

			switch (option) {
				case 1:
					account = loginIntoAccount();
					if (account != null) {
						showAccountMenu(account);
					}

					break;
				case 2:
					account = registerNewAccount();
					if (account != null) {
						showAccountMenu(account);
					}

					break;
				case 3:
					break loop;
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

	private static Account loginIntoAccount() {
		System.out.println("Certo! Então me diga o seu CPF:");
		String cpf = scan.nextLine();

		AccountHolder accountHolder = ahc.findByCpf(cpf);
		if (accountHolder == null) {
			System.out.println("Não encontramos nenhum usuário com esse CPF :(");
			return null;
		}

		List<Account> accounts = ac.getAccountsByCpf(cpf);
		if (accounts.isEmpty()) {
			System.out.println("Não encontramos nenhuma conta associada a esse CPF :(");
			return null;
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
				return account;
			} else {
				System.out.println(account.getPassword());
				System.out.println(hashedPassword);
				System.out.println("Senha incorreta! Tente novamente.");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Algum erro aconteceu! Não foi possível acessar a conta");
			return null;
		}
	}

	private static Account registerNewAccount() {
		System.out.println("Certo! Então me diga o seu CPF:");
		String cpf = scan.nextLine();
		
		if (ac.isMaximumAccountNumberReached(cpf)) {
			System.out.println("Número máximo de contas atingido. Você só pode ter 3 contas");
			return null;
		}

		AccountHolder accountHolder = ahc.findByCpf(cpf);
		if (accountHolder == null) {
			System.out.println("Não encontramos nenhum usuário com esse CPF :(");
			return null;
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
			return null;
		}

		account = ac.store(account);
		System.out.println("Conta criada com sucesso!");
		return account;
	}

	private static void showAccountMenu(Account account) {
		int option;
	
		while (true) {
			System.out.println("\nO que você deseja fazer?");
			System.out.println("1. Visualizar saldo\n2. Realizar transação\n3. Extrato\n4. Sair");
	
			option = scan.nextInt();
			scan.nextLine();
	
			switch (option) {
				case 1:
					viewBalance(account);
					break;
				case 2:
					performTransaction(account);
					break;
				case 3:
					showStatementMenu(account);
					break;
				case 4:
					System.out.println("Saindo...");
					return;
				default:
					System.out.println("Opção inválida. Tente novamente.");
					break;
			}
		}
	}
	
	private static void viewBalance(Account account) {
		System.out.println("Saldo atual: R$" + tc.getBalance(account.getId()));
	}

	private static void performTransaction(Account account) {
		int option;

		while (true) {
			System.out.println("Qual tipo de transação deseja realizar?");
			System.out.println("1. Depósito\n2. Saque\n3. Transferência\n4. Cartão de Débito\n5. PIX\n6. Voltar");

			option = scan.nextInt();
			scan.nextLine();

			if (option == 6) {
				return;
			}
			
			System.out.println("Informe o valor da transação:");
			double amount = scan.nextDouble();
			scan.nextLine();

			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setDate(new Date());
			transaction.setValue(amount);

			switch (option) {
				case 1:
					transaction.setType(TransactionType.DEPOSIT);
					transaction.setDescription(TransactionType.DEPOSIT + " transaction realized!");
					break;
				case 2:
					transaction.setType(TransactionType.WITHDRAW);
					transaction.setDescription(TransactionType.WITHDRAW + " transaction realized!");
					break;
				case 3:
					transaction.setType(TransactionType.PAYMENT);
					transaction.setDescription(TransactionType.PAYMENT + " transaction realized!");
					break;
				case 4:
					transaction.setType(TransactionType.DEBIT_CARD);
					transaction.setDescription(TransactionType.DEBIT_CARD + " transaction realized!");
					break;
				case 5:
					transaction.setType(TransactionType.PIX);
					transaction.setDescription(TransactionType.PIX + " transaction realized!");
					break;
				default:
					System.out.println("Opção inválida. Tente novamente.");
					break;
			}
			
			transaction = tc.store(transaction);
			
			if (transaction != null) {
				System.out.println("Transação realizada com sucesso!");
			} else System.out.println("Algum erro aconteceu, transação cancelada");
		}
	}

	private static void showStatementMenu(Account account) {}
}

# Modular Banking System

A modular, test-driven Java banking system that models core retail bank operations and validates user commands through a clean validator/processor pipeline.

> Built with Gradle; 100% Java. Includes extensive unit tests and mutation testing setup.

## ✨ Features

- **Account types:** `CheckingAccount`, `SavingsAccount`, `CDAccount`, all extending a common `Account` abstraction.
- **Command pipeline:** `CommandValidator` family for input validation; `CommandProcessor` to execute valid commands; `MasterControl` orchestrates processing and storage of invalid commands (via `CommandStorage`).
- **Commands covered:** create, deposit, and “pass time” (validators + tests). Add others (withdraw, transfer) as you implement.
- **Testing:** JUnit tests across validators, processor, and control layer; mutation testing configured (e.g., PIT).
- **CI ready:** Project previously used GitLab CI (`.gitlab-ci.yml`)—easy to map to GitHub Actions.

## 🏗️ Architecture

```text
src/
  main/
    java/
      .../accounts/        # Account hierarchy (Checking, Savings, CD)
      .../commands/        # Command models (Create, Deposit, PassTime)
      .../validation/      # CommandValidator and specific validators
      .../processing/      # CommandProcessor (executes valid commands)
      .../control/         # MasterControl (orchestration) + CommandStorage
      .../bank/            # Bank aggregate, repository-style storage
  test/
    java/
      ...                  # Unit tests for validators, processor, control

```
> Adjust package names if they differ in your project.

## 🧪 Command Model

Supported and tested commands:

- `create ...`
- `deposit ...`
- `withdraw ..`
- `transfer ...`
- `pass time ...`

(Expand list as you add new commands.)

## 🚀 Getting Started

### Prerequisites
- Java 17+ (recommended)
- Git
- No need to install Gradle globally—project includes the Gradle Wrapper.

### Build

```bash
./gradlew clean build

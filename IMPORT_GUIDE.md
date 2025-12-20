# Guia de Importação do Projeto

## 📦 Nome do Projeto
**minimanager-products-service**

## 🎯 Como Importar nas IDEs

### IntelliJ IDEA (Recomendado)

#### Opção 1: Via Menu
1. Abra o IntelliJ IDEA
2. Clique em **File → Open**
3. Navegue até a pasta:
   ```
   d:\tools\source\java\projetoaplicado\SP205 - Postagem da Sprint 2\minimanager-products
   ```
4. Selecione a pasta (não o build.gradle) e clique em **OK**
5. IntelliJ detectará automaticamente o projeto Gradle
6. Na janela "Import Project", selecione:
   - ✅ Use auto-import
   - ✅ Gradle JVM: 11 ou superior
7. Clique em **OK**
8. Aguarde o download das dependências (primeira vez demora ~2-5 minutos)

#### Opção 2: Importar Projeto
1. **File → New → Project from Existing Sources**
2. Selecione a pasta do projeto
3. Escolha **"Import project from external model"**
4. Selecione **Gradle**
5. Configure:
   - Gradle JVM: Java 11+
   - Use Gradle wrapper
6. Finish

#### Verificar Importação
- Abra a aba **Gradle** (lado direito)
- Clique no ícone de refresh (🔄)
- Expanda `minimanager-products-service → Tasks`
- Deve aparecer: application, build, verification, etc.

---

### Eclipse (STS - Spring Tool Suite)

#### Passo 1: Instalar Plugin Buildship (se não tiver)
1. **Help → Eclipse Marketplace**
2. Busque por "Buildship Gradle"
3. Instale a versão 3.x

#### Passo 2: Importar Projeto
1. **File → Import**
2. Expanda **Gradle**
3. Selecione **Existing Gradle Project**
4. **Next**
5. Em "Project root directory", clique em **Browse**
6. Selecione:
   ```
   d:\tools\source\java\projetoaplicado\SP205 - Postagem da Sprint 2\minimanager-products
   ```
7. **Next** → **Next**
8. Em "Gradle wrapper", deixe marcado:
   - ✅ Use specific Gradle distribution (Gradle wrapper)
9. **Finish**

#### Verificar Importação
- No **Project Explorer**, clique com botão direito no projeto
- **Gradle → Refresh Gradle Project**
- Abra a view **Gradle Tasks** (Window → Show View → Other → Gradle Tasks)

---

### VS Code

#### Pré-requisitos (Extensões)
1. **Extension Pack for Java** (Microsoft)
2. **Spring Boot Extension Pack** (VMware)
3. **Gradle for Java** (Microsoft)

#### Importar
1. Abra o VS Code
2. **File → Open Folder**
3. Selecione a pasta `minimanager-products`
4. VS Code detectará automaticamente o projeto
5. Caso peça, selecione a JDK (Java 11+)

#### Executar
- Abra a paleta de comandos: `Ctrl+Shift+P`
- Digite: **"Spring Boot Dashboard: Open"**
- Clique no ícone de play ▶️ ao lado do projeto

---

## 🚀 Comandos Úteis (Terminal)

### Build do Projeto
```bash
# Windows
gradlew.bat build

# Linux/Mac
./gradlew build
```

### Executar Aplicação
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### Limpar Build
```bash
./gradlew clean
```

### Ver Tasks Disponíveis
```bash
./gradlew tasks
```

### Rodar Testes
```bash
./gradlew test
```

---

## ⚙️ Configuração da JDK

### IntelliJ
1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. **Project Settings → Project**
3. **SDK:** Selecione JDK 11 ou superior
4. **Language level:** 11

### Eclipse
1. Clique com botão direito no projeto
2. **Properties → Java Build Path**
3. Aba **Libraries**
4. **Edit** JRE System Library
5. Selecione JDK 11+

### VS Code
1. `Ctrl+Shift+P`
2. **"Java: Configure Java Runtime"**
3. Selecione JDK 11+ para o projeto

---

## ✅ Validação

Após importar, verifique:

1. **Dependências resolvidas:**
   - Não deve haver erros de importação no código
   - Classes Spring Boot (`@SpringBootApplication`) devem estar reconhecidas

2. **Estrutura do projeto:**
   ```
   minimanager-products/
   ├── src/
   │   ├── main/
   │   │   ├── java/
   │   │   │   └── br/com/xpeducacao/.../produtos/
   │   │   └── resources/
   │   │       └── application.properties
   │   └── test/
   ├── build.gradle          ✅ Arquivo principal
   ├── settings.gradle       ✅ Nome do projeto
   ├── gradle.properties     ✅ Configurações
   └── gradlew / gradlew.bat ✅ Scripts wrapper
   ```

3. **Build bem-sucedido:**
   ```bash
   ./gradlew build
   ```
   Deve terminar com: `BUILD SUCCESSFUL`

4. **Aplicação executável:**
   ```bash
   ./gradlew bootRun
   ```
   Acesse: http://localhost:8082

---

## 🐛 Problemas Comuns

### Erro: "Could not find Java 11"
- Instale JDK 11 ou superior
- Configure a variável `JAVA_HOME`

### Erro: "Permission denied" (Linux/Mac)
```bash
chmod +x gradlew
```

### Dependências não baixam
```bash
./gradlew clean build --refresh-dependencies
```

### IDE não reconhece o Gradle
- Delete as pastas `.gradle` e `build`
- Reimporte o projeto

---

## 📞 Suporte

Problemas? Verifique:
1. JDK 11+ instalado e configurado
2. Gradle wrapper presente (gradlew/gradlew.bat)
3. Conexão com internet (primeira build baixa dependências)
4. Logs do Gradle: `./gradlew build --info`

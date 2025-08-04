# Teste Prático Leal Apps
## Marcos Andrade - **`GYM HERO`**

Este projeto foi desenvolvido com foco em boas práticas de engenharia de software, priorizando uma arquitetura sólida, código limpo, usabilidade e integração eficiente com o Firebase.

---

## ✅ Requisitos e Implementações

### 1. **Boas Práticas de Programação**
- Utilização de **nomes claros e consistentes** para variáveis e funções.
- **Componentização** de telas e lógicas para promover reuso e fácil manutenção.
- Separação clara de responsabilidades com foco em **código limpo e escalável**.

### 2. **Arquitetura MVVM + Repository**
- Estrutura baseada no padrão **MVVM (Model-View-ViewModel)**.
- Uso do **Repository Pattern** como camada intermediária entre ViewModel e Firebase.
- Proporciona **testabilidade, desacoplamento e organização**.

### 3. **Coroutines + Firebase (viewModelScope + Dispatchers.IO)**
- Comunicação assíncrona com o Firebase utilizando **coroutines e funções `suspend`**.
- Chamadas executadas via **`viewModelScope.launch(Dispatchers.IO)`**, mantendo a UI fluida.
- Garante desempenho adequado em operações intensivas como leitura, escrita e upload de dados/imagens.

### 4. **Experiência do Usuário**
- Interface **moderna, responsiva e intuitiva**, construída com **Jetpack Compose**.
- Feedback visual claro em carregamentos e interações.
- Navegação fluida entre telas com **Navigation Compose**.

### 5. **Autenticação de Usuário (Firebase Auth)**
- Sistema de login e cadastro com **e-mail e senha** via **Firebase Authentication**.
- Sessão persistente para o usuário autenticado.
- Proteção de rotas e dados sensíveis com verificação de autenticação.

### 6. **Banco de Dados (Firestore)**
- Estrutura de dados organizada por **UserID**, garantindo privacidade e isolamento por usuário.
- Utilização do **Firebase Firestore** para armazenar treinos e exercícios.

### 7. **Armazenamento de Imagens (Firebase Storage)**
- Upload e recuperação de imagens utilizando **Firebase Storage**.
- As URLs das imagens são armazenadas no Firestore, facilitando a exibição nas interfaces.

---

## 📂 Tecnologias e Ferramentas Utilizadas

- **Kotlin** + **Jetpack Compose**
- **Android Studio**
- **MVVM Architecture** + **Repository Pattern**
- **Firebase Auth**, **Firestore** e **Storage**
- **Kotlin Coroutines** (`suspend`, `viewModelScope.launch(Dispatchers.IO)`)
- **Navigation Compose**
- **MutableState** + **StateFlow** para gerenciamento de estado reativo

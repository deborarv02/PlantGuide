# 🌿 PlantGuide

Aplicativo Android de guia de plantas desenvolvido como **Projeto Final** da disciplina **ADS — Desenvolvimento Mobile II (2026/1)** da Universidade de Passo Fundo (UPF).

---

## 📋 Descrição

O **PlantGuide** permite ao usuário:
- Navegar por um catálogo de plantas com informações detalhadas
- Filtrar plantas por categoria e buscar por nome
- Favoritar plantas para acesso rápido
- **Identificar plantas em tempo real** apontando a câmera — a IA (Gemini Vision) reconhece a espécie e retorna nome popular, nome científico, família botânica, origem, nível de luz, frequência de rega, ambiente ideal, cuidados básicos, toxicidade para pets e uma curiosidade

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Uso |
|---|---|
| Kotlin | Linguagem principal |
| Android Studio | IDE de desenvolvimento |
| Room (SQLite) | Persistência local — CRUD completo |
| ViewModel + LiveData | Arquitetura MVVM |
| CameraX | Câmera em tempo real |
| Gemini Vision API | Identificação de plantas por IA |
| Material Design 3 | UI/UX |
| ViewBinding | Binding de layouts |
| RecyclerView + DiffUtil | Lista de plantas |
| Navigation Drawer | Navegação entre telas |
| Fragments | PlantListFragment na HomeActivity |

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)**:

```
com.plantguide
├── data/
│   ├── entity/         → Plant.kt (entidade Room)
│   ├── dao/            → PlantDao.kt (interface CRUD)
│   ├── database/       → PlantDatabase.kt + PlantDataSource.kt
│   └── PlantRepository.kt
├── ui/
│   ├── PlantViewModel.kt
│   ├── splash/         → SplashActivity
│   ├── login/          → LoginActivity
│   ├── home/           → HomeActivity + PlantListFragment + PlantAdapter
│   ├── detail/         → PlantDetailActivity
│   ├── favorites/      → FavoritesActivity
│   ├── scan/           → PlantScanActivity (câmera + IA)
│   └── about/          → AboutActivity
└── util/
    └── PreferenceHelper.kt
```

---

## ▶️ Instruções de Execução

1. Clone o repositório
2. Abra o projeto no **Android Studio**
3. No arquivo `app/src/main/res/values/strings.xml`, substitua:
   ```xml
   <string name="gemini_api_key">SUA_CHAVE_GEMINI_AQUI</string>
   ```
   pela sua chave obtida em [https://aistudio.google.com](https://aistudio.google.com)
4. Execute em um dispositivo físico ou emulador com **Android 7.0+ (API 24)**

> **Nota:** A funcionalidade de identificação de plantas requer dispositivo físico com câmera e conexão com a internet.

---

## 📱 Telas do Aplicativo

| Tela | Descrição |
|---|---|
| Splash | Tela inicial com logo e verificação de login |
| Login | Autenticação simulada (email + senha ≥ 6 chars) |
| Catálogo | Lista de plantas com busca e filtro por categoria |
| Detalhe | Informações completas da planta + favoritar |
| Favoritas | Lista de plantas favoritadas |
| Identificar Planta | Câmera em tempo real + IA Gemini |
| Sobre | Informações sobre o app e a disciplina |

---

## 👥 Integrantes

> _(Adicione os nomes dos integrantes da dupla aqui)_

---

## 📚 Disciplina

**ADS — Desenvolvimento Mobile II**  
Semestre: 2026/1  
Professores: Diego A. Lusa e Jaqson Dalbosco  
Universidade de Passo Fundo — UPF

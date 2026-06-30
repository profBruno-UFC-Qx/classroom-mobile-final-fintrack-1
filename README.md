[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/AR7CADm8)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23497911)
# :checkered_flag: Fintrack
Fintrack é um tracker financeiro para pessoas que desejam gerir melhor sua vida financeira.

## :technologist: Membros da equipe

- 493973, Gabriel Ileis Araújo Vieira, Ciência da Computação
- 570598, Pedro Vittor Moreira Sampaio, Ciência da Computação
- 514087, Vitor Nayan Azevedo Lopes, Engenharia de Software

## :bulb: Objetivo Geral
O objetivo geral do Fintrack é educar financeiramente jovens que estão tendo contato com dinheiro pela primeira vez, fornecendo uma ferramenta fácil de usar para controlar gastos, monitorar estatísticas e tomar decisões financeiras informadas.

## :eyes: Público-Alvo
O público alvo do Fintrack são jovens entre 18 e 25 anos que estão começando a gerenciar suas finanças pessoais, incluindo estudantes universitários, jovens profissionais e aqueles que estão iniciando sua vida financeira de forma independente.

## :star2: Impacto Esperado
O impacto esperado do Fintrack é:
- Aumentar a conscientização financeira entre jovens, ajudando-os a entender melhor como gerenciar seu dinheiro;
- Reduzir a dependência de crédito e aumentar a poupança;
- Melhorar a capacidade de tomar decisões financeiras informadas;
- Fornecer uma ferramenta para monitorar e controlar gastos, ajudando a prevenir problemas financeiros.

## :triangular_flag_on_post: Principais funcionalidades da aplicação
As principais funcionalidades do Fintrack incluem:
- Controle de gastos: permitir que os usuários registrem e monitorem seus gastos diários;
- Estatísticas e relatórios: fornecer estatísticas e relatórios sobre os gastos, incluindo gráficos e tabelas;
- Notícias e Dicas: integração com API externa para exibição de notícias do mercado financeiro em tempo real em uma gaveta lateral;
- Orçamento: permitir que os usuários definam e monitorem orçamentos para diferentes categorias de gastos;
- Alertas e notificações: enviar alertas e notificações para os usuários lembrarem de atualizar os gastos no aplicativo.
---

> [!WARNING]
> Daqui em diante o README.md só deve ser preenchido no momento da entrega final.

##  Tecnologias: 
Liste aqui as tecnologias e bibliotecas que foram utilizadas no projeto.

- Jetpack Compose (Material 3)
- Room Database (Persistência)
- Retrofit & GSON (Consumo de API)
- WorkManager (Agendamento de Tarefas)
- Vico Charts (Visualização de Dados)
- Kotlin Coroutines & Flow (Reatividade)
- MVVM (Arquitetura)
---

## Instruções para Execução
[Inclua instruções claras sobre como rodar o projeto localmente. Isso é crucial para que você possa testá-lo nas próximas entregas. **Somente caso haja alguma coisa diferente do usual**

```bash
# Clone o repositório
git clone https://github.com/profBruno-UFC-Qx/classroom-mobile-final-fintrack-1.git

# Navegue para o diretório
cd classroom-mobile-final-fintrack-1

# 1. Cadastro na API de Notícias:
# Este projeto utiliza o serviço NewsData.io para exibir notícias reais.
# É necessário cadastrar-se gratuitamente em https://newsdata.io/ para obter uma API KEY.

# 2. Configuração da Chave (IMPORTANTE):
# Por questões de segurança, a chave não fica no código. 
# Na raiz do projeto, localize ou crie o arquivo 'local.properties' e adicione a linha:
NEWS_API_KEY = insira_aqui_sua_chave_obtida_no_site

# 3. Build e Execução:
# Abra o projeto no Android Studio, realize o 'Sync Project with Gradle Files' 
# e execute em um dispositivo ou emulador com Android 8.0 (API 26) ou superior.

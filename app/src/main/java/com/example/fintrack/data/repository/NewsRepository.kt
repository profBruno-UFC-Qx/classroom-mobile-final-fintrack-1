package com.example.fintrack.data.repository

import com.example.fintrack.BuildConfig
import com.example.fintrack.data.api.NewsApiService
import com.example.fintrack.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository(private val apiService: NewsApiService) {

    private val API_KEY = BuildConfig.NEWS_API_KEY

    fun getFinancialNews(): Flow<List<News>> = flow {
        try {
            val response = apiService.getFinancialNews(apiKey = API_KEY)
            if (response.status == "success") {
                emit(response.results + getTips())
            } else {
                emit(getTips())
            }
        } catch (e: Exception) {
            emit(getTips())
        }
    }

    private fun getTips() = listOf(
        News(
            "Regra 50-30-20",
            "Divida sua renda em três partes: 50% para necessidades (aluguel, comida, transporte), 30% para desejos e 20% para poupança ou dívidas. É um dos métodos mais usados no mundo.",
            "",
            "Fintrack Tips"
        ),
        News(
            "Anote tudo, sem exceção",
            "Pequenos gastos como café, passagem e delivery parecem irrelevantes, mas somados podem representar até 20% da sua renda. Registrar tudo é o primeiro passo para entender seu dinheiro.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "Reserva de emergência primeiro",
            "Antes de investir ou comprar algo por impulso, construa uma reserva de 3 a 6 meses dos seus gastos fixos em um investimento de liquidez diária, como o Tesouro Selic ou CDB com resgate diário.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "Cuidado com o crédito rotativo",
            "Pagar o mínimo do cartão de crédito é uma das armadilhas financeiras mais perigosas. Os juros do rotativo no Brasil chegam a 400% ao ano. Se não puder pagar tudo, prefira o parcelamento.",
            "",
            "Educação Financeira"
        ),
        News(
            "O poder dos juros compostos",
            "Investir R$ 200 por mês a partir dos 20 anos pode gerar muito mais do que investir R$ 400 a partir dos 35. Começar cedo é mais importante do que investir muito tarde.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "Espere 48 horas antes de comprar",
            "Para compras não planejadas acima de R$ 100, espere 48 horas. Na maioria dos casos, a vontade passa. Essa técnica simples evita compras por impulso que comprometem o orçamento.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "Negocie suas contas fixas",
            "Internet, celular e streaming têm planos melhores do que o seu? Ligue e peça desconto. Empresas preferem renegociar a perder o cliente. Muita gente economiza R$ 50 a R$ 150 por mês só com isso.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "Diferencie desejo de necessidade",
            "Necessidade é o que você precisa para viver: moradia, alimentação, saúde e transporte. Desejo é tudo que melhora sua qualidade de vida mas não é essencial. Saber essa diferença muda tudo.",
            "",
            "Educação Financeira"
        ),
        News(
            "Automatize sua poupança",
            "Configure uma transferência automática para uma conta separada assim que o salário cair. Guardar o que sobra quase nunca funciona — o segredo é gastar o que sobra depois de guardar.",
            "",
            "Fintrack Dicas"
        ),
        News(
            "O que é a Selic e por que importa",
            "A Selic é a taxa básica de juros do Brasil. Quando ela sobe, investimentos em renda fixa rendem mais e crédito fica mais caro. Acompanhar a Selic ajuda a tomar melhores decisões financeiras.",
            "",
            "Educação Financeira"
        )
    )
}
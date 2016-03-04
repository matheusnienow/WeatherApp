WeatherApp - Matheus Navarro Nienow

SDK m�nima: 15

Bibliotecas:
    'com.android.support:appcompat-v7:23.1.1'
    'com.android.support:design:23.1.1'
    'com.mcxiaoke.volley:library:1.0.19'
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Tela principal: 
	- MainActivity ("Previs�o hoje"). 
	- Possui uma lista (ListView) com a previs�o das cidades cadastradas.
	- Possui um bot�o (Floating Action Button) para cadastrar novas cidades.
	- Possui uma op��o "Ordenar" no menu superior, que permite ordenar a lista alfabeticamente ou pela temperatura.

	- Clique (onClick) simples nos itens da lista leva para a tela de previs�o semanal (PrevisaoActivity).
	- Clique longo (onLongClick) nos itens da lista d� a op��o de deletar a cidade.
	- Arrastar (onRefresh) a lista para baixo atualiza �s informa��es de previs�o.

Tela cadastro:
	- CadastroActivity ("Cadastrar cidade").
	- Acessada pelo bot�o de cadastro na tela principal (MainActivity).
	- Possui uma caixa de inser��o (EditText) para digitar o nome da cidade � cadastrar.
	- Possui um bot�o (Button) para realizar o cadastro.
	
	- Abaixo do layout de cadastro h� um breve guia de cadastro, explicando como deve ser feito o cadastro das cidades.

Tela de previs�o:
	- Acessada atrav�s de um clique longo na lista de cidades da tela principal (MainActivity).
	- Possui um cabe�alho com a previs�o atual da cidade;
	- Abaixo do cabe�alho h� uma lista com a previs�o de 5 dias (incluindo o dia atual), mostrando a temperatura m�xima e m�nimo de cada dia.
	- Arrastar (onRefresh) a lista para baixo atualiza �s informa��es de previs�o.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Pacotes:
	
	- activity: possui as classes Activity do projeto.
	- adapter: possui as classes Adapter das listas.	
	- model: possui as classes modelo de informa��es da API OpenWeather que s�o comuns �s duas previs�es, atual e semanal. (?http://openweathermap.org/api?).
	- parser: possui a classe parser que transforma a resposta JSON em inst�ncia das classes modelos.
	- thread: possui as classes que extendem threads, utilizadas para download das imagens das previs�es.
	- util: possui a classe Util para m�todos de utilidade geral no projeto.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Layout:
	- O layout das activities foram dividos em dois arquivos: activity_nome.xml, para a estrutura geral da activity (Toolbar, FAB Button, etc.) 
	e content_nome.xml, para o conte�do da activity (ListView, EditText, Button, etc.).
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Vis�o geral do funcionamento:
	
	- Para cadastrar uma cidade, o app realiza um request para � api utilizando o nome da cidade como par�metro. A resposta � transformada em um 
	objeto DailyForecast pela classe JSONParser. 
		- Esses objetos s�o armazenado em um ArrayList dentro da classe Adapter. Esses objetos tamb�m s�o serializados e salvos em um arquivo com o nome 
		"daily"+{DailyForecast.id}.

		- � criado tamb�m um arquivo chamado "fileNames" que armazena o nome dos arquivos DailyForecast.

		- As imagens das previs�es s�o baixadas paralelamente atrav�s de outro request para o servidor da api. Ap�s serem baixado s�o salvos em um arquivo 
		{nomeArquivoDaily}+"img".

	- A tela principal verifica se existe alguma informa��o salva nos arquivo e alimenta � lista de cidades com essas informa��es, caso n�o haja nenhuma,
	� feito um novo request.

	- Para atualizar �s informa��es, o Adapter faz uma c�pia do ArrayList e para cada item: delete o mesmo e faz um novo request. Caso n�o
	haja nenhum erro, o ArrayList original � substitu�do pelo novo atualizado, caso contr�rio o antigo � mantido.

	- Ao clicar numa cidade, o objeto do tipo DailyForecast � passado pela intent para a activity PrevisaoActivity.

	- � feito ent�o um request para a API utilizando o id da cidade.

	- O cache da previs�o semanal � feita de maneira parecida com o da previs�o atual, por�m para cada cidade � criada uma pasta com o nome {idCidade}, onde
	dentro da pasta � salvo o objeto do tipo WeekForecast com o mesmo nome da pasta e todas �s respectivas imagens das previs�es, �s imagens s�o salvas com o 

	- Para atualizar a previs�o semanal � apenas feito um novo request, pois todas as inforam��es ficam armazenadas no objeto do tipo WeekForecast.
	nome img+{i}.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Observa��es/considera��es finais:

	- Eu nunca havia trabalho com conectividade do tipo Request, ent�o tive que estudar um pouco, por�m foi bem simples.
	- Tamb�m nunca havia trabalhado com JSON, tentei utilizar a biblioteca GSON, mas por algum motivo eu n�o consegui transformar o JSON nos objetos modelos, 
	ent�o fiz as convers�es "manualmente".
	- Eu tamb�m nunca tinha feito um sistema de cache, foi o que mais deu trabalho para fazer.
	- Fiz testes manuais, n�o sou familiarizado com testes JUnit.
	- Tentei desenvolver o aplicativo com o m�nimo de ajuda poss�vel para que possam avaliar o meu n�vel de conhecimento.
	- Testei o app apenas no meu aparelho, Samsung Galaxy S5.










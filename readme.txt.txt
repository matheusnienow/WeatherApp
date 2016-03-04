WeatherApp - Matheus Navarro Nienow

SDK mínima: 15

Bibliotecas:
    'com.android.support:appcompat-v7:23.1.1'
    'com.android.support:design:23.1.1'
    'com.mcxiaoke.volley:library:1.0.19'
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Tela principal: 
	- MainActivity ("Previsão hoje"). 
	- Possui uma lista (ListView) com a previsão das cidades cadastradas.
	- Possui um botão (Floating Action Button) para cadastrar novas cidades.
	- Possui uma opção "Ordenar" no menu superior, que permite ordenar a lista alfabeticamente ou pela temperatura.

	- Clique (onClick) simples nos itens da lista leva para a tela de previsão semanal (PrevisaoActivity).
	- Clique longo (onLongClick) nos itens da lista dá a opção de deletar a cidade.
	- Arrastar (onRefresh) a lista para baixo atualiza às informações de previsão.

Tela cadastro:
	- CadastroActivity ("Cadastrar cidade").
	- Acessada pelo botão de cadastro na tela principal (MainActivity).
	- Possui uma caixa de inserção (EditText) para digitar o nome da cidade à cadastrar.
	- Possui um botão (Button) para realizar o cadastro.
	
	- Abaixo do layout de cadastro há um breve guia de cadastro, explicando como deve ser feito o cadastro das cidades.

Tela de previsão:
	- Acessada através de um clique longo na lista de cidades da tela principal (MainActivity).
	- Possui um cabeçalho com a previsão atual da cidade;
	- Abaixo do cabeçalho há uma lista com a previsão de 5 dias (incluindo o dia atual), mostrando a temperatura máxima e mínimo de cada dia.
	- Arrastar (onRefresh) a lista para baixo atualiza às informações de previsão.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Pacotes:
	
	- activity: possui as classes Activity do projeto.
	- adapter: possui as classes Adapter das listas.	
	- model: possui as classes modelo de informações da API OpenWeather que são comuns às duas previsões, atual e semanal. (?http://openweathermap.org/api?).
	- parser: possui a classe parser que transforma a resposta JSON em instância das classes modelos.
	- thread: possui as classes que extendem threads, utilizadas para download das imagens das previsões.
	- util: possui a classe Util para métodos de utilidade geral no projeto.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Layout:
	- O layout das activities foram dividos em dois arquivos: activity_nome.xml, para a estrutura geral da activity (Toolbar, FAB Button, etc.) 
	e content_nome.xml, para o conteúdo da activity (ListView, EditText, Button, etc.).
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Visão geral do funcionamento:
	
	- Para cadastrar uma cidade, o app realiza um request para à api utilizando o nome da cidade como parâmetro. A resposta é transformada em um 
	objeto DailyForecast pela classe JSONParser. 
		- Esses objetos são armazenado em um ArrayList dentro da classe Adapter. Esses objetos também são serializados e salvos em um arquivo com o nome 
		"daily"+{DailyForecast.id}.

		- É criado também um arquivo chamado "fileNames" que armazena o nome dos arquivos DailyForecast.

		- As imagens das previsões são baixadas paralelamente através de outro request para o servidor da api. Após serem baixado são salvos em um arquivo 
		{nomeArquivoDaily}+"img".

	- A tela principal verifica se existe alguma informação salva nos arquivo e alimenta à lista de cidades com essas informações, caso não haja nenhuma,
	é feito um novo request.

	- Para atualizar às informações, o Adapter faz uma cópia do ArrayList e para cada item: delete o mesmo e faz um novo request. Caso não
	haja nenhum erro, o ArrayList original é substituído pelo novo atualizado, caso contrário o antigo é mantido.

	- Ao clicar numa cidade, o objeto do tipo DailyForecast é passado pela intent para a activity PrevisaoActivity.

	- É feito então um request para a API utilizando o id da cidade.

	- O cache da previsão semanal é feita de maneira parecida com o da previsão atual, porém para cada cidade é criada uma pasta com o nome {idCidade}, onde
	dentro da pasta é salvo o objeto do tipo WeekForecast com o mesmo nome da pasta e todas às respectivas imagens das previsões, às imagens são salvas com o 

	- Para atualizar a previsão semanal é apenas feito um novo request, pois todas as inforamções ficam armazenadas no objeto do tipo WeekForecast.
	nome img+{i}.
------------------------------------------------------------------------------------------------------------------------------------------------------------------
Observações/considerações finais:

	- Eu nunca havia trabalho com conectividade do tipo Request, então tive que estudar um pouco, porém foi bem simples.
	- Também nunca havia trabalhado com JSON, tentei utilizar a biblioteca GSON, mas por algum motivo eu não consegui transformar o JSON nos objetos modelos, 
	então fiz as conversões "manualmente".
	- Eu também nunca tinha feito um sistema de cache, foi o que mais deu trabalho para fazer.
	- Fiz testes manuais, não sou familiarizado com testes JUnit.
	- Tentei desenvolver o aplicativo com o mínimo de ajuda possível para que possam avaliar o meu nível de conhecimento.
	- Testei o app apenas no meu aparelho, Samsung Galaxy S5.










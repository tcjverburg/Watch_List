# Code review

##### Code: Tom Verburg
##### Review: Sidney de Vries
### Names: 3 
Variabelen en functies hebben duidelijke namen, als je de naam leest dan heb je al een idee wat een functie gaat doen. Verder is er ook consistentie in hoofdlettergebruik, en beginnen variabelen niet met hoofdletters. Een minpuntje is de referentie naar id's van views, die meestal gewoon standaard 'textview1' of 'edittext' is. Door views een duidelijke id te geven word de code nog net iets duidelijker.

### Headers: 3/4 
Alle bestanden hebben headers, deze headers zijn misschien 'wordy' maar er word wel duidelijk gezegd wat er in de code gebeurt.

### Comments: 3 
Alle functies hebben duidelijke concrete comments die goed uitleggen wat er in de code gebeurt. In sommige gevallen is dit niet heel nuttig omdat de naam van een funtie meestal al genoeg uitleg geeft, maar de comments zorgen wel voor meer verklaring.

### Layout: 3 
Er is een duidelijke structuur in de code, in sommige gevallen is er nog een standaard android studio comment te vinden met 'Created by Gebruiker on 25-9-2016.' maar voor de rest staan er geen oude stukken code of nutteloze comments in. Ook is de structuur van de code duidelijk. 

### Formatting: 2/3 
In de code staan soms wat witregels die er eigenlijk niet moeten staan (vb. aan eind van MainActivity) en is er op een plek sprake van 2 blokken code die ook samengevoegd kunnen worden in 1 blok code (dubbele catch kan aan elkaar geplakt worden met ||)

### Flow: 3 
Er is een duidelijke flow, er is geen sprake onbereikbare code voor zover ik kan zien. De flow word ook duidelijk door modularisatie waardoor deel van code uit zicht blijft en overzichtelijkheid behouden blijft.

### Expressions: 3 
De expressies zijn duidelijk en ze zijn allemaal relevant (geen overbodige stukken code) datatypes komen overeen met wat ze moeten doen (misschien lastige manier gekozen bij het extraccten van de json, had makkelijker gekund door bijvoorbeeld JSONobject)

### Decomposition: 2/3 
Overal is dit wel goed, behalve bij de async task die 2 keer hetzelfde is maar ook in 2 verschillende bestanden word aangeroepen. Het was handiger geweest om een aparte async task class te maken en die in de andere classes aan te roepen. Scheelt veel code en maakt alles overzichtelijker.

### Modularization: 2/3 
Reden voor 2 is hierboven genoemd, namelijk de asynctask ie dubbel in de code staat, maar voor de rest hebben alle modules 1 duidelijke taak en hebben ze geen invloed op stukken code waar ze ook geen invloed op zouden moeten hebben.



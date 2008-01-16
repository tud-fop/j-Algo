<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE helpset
PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">


	<!-- title -->
	<title>Hilfe zum Pulsemem-Modul</title>


	<!-- maps -->
	<maps>
		<homeID>pulsemem</homeID>
		<mapref location="map.jhm"/>
	</maps>


	<!-- presentations -->
	<presentation default=true>
		<name>main window</name>
		<size width="920" height="450" />
		<location x="150" y="150" />
		<image>mainicon</image>

		<toolbar>
			<helpaction>javax.help.BackAction</helpaction>
			<helpaction>javax.help.ForwardAction</helpaction>
			<helpaction>javax.help.ReloadAction</helpaction>
			<helpaction>javax.help.SeparatorAction</helpaction>
			<helpaction image="Startseite">javax.help.HomeAction</helpaction>
			<helpaction>javax.help.SeparatorAction</helpaction>
			<helpaction>javax.help.PrintAction</helpaction>
			<helpaction>javax.help.PrintSetupAction</helpaction>
		</toolbar>
	</presentation>


	<!-- views -->
  	<view mergetype="javax.help.UniteAppendMerge">
		<name>TOC</name>
		<label>Inhaltsverzeichnis</label>
		<type>javax.help.TOCView</type>
		<data>pulsememTOC.xml</data>
	</view>

	<view mergetype="javax.help.SortMerge">
    	<name>Index</name>
    	<label>Index</label>
    	<type>javax.help.IndexView</type>
    	<data>pulsememIndex.xml</data>
  	</view>

	<view xml:lang="de">
		<name>Search</name>
		<label>Suche</label>
		<type>javax.help.SearchView</type>
			<data engine="com.sun.java.help.search.DefaultSearchEngine">
      		JavaHelpSearch
    		</data>
	</view>

</helpset>
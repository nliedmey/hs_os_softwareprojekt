module speeddatingrep {
	exports de.swprojekt.speeddating.launcher;	//das Modul stellt dieses Package bereit

	requires spring.boot;	//das Modul benoetigt folgende andere Module
	requires spring.boot.autoconfigure;
	requires spring.context;
	requires spring.web;
}
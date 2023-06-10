module osiris.contract {
	requires transitive java4ever.framework;
	exports tech.deplant.osiris.contract;
	exports tech.deplant.osiris.template;
	exports tech.deplant.osiris;
	opens tech.deplant.osiris.contract to com.fasterxml.jackson.databind;
	opens tech.deplant.osiris.template to com.fasterxml.jackson.databind;
}
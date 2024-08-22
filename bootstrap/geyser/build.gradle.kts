plugins {
    id("com.rtm516.mcxboxbroadcast.shadow-conventions")
}

relocate("org.yaml.snakeyaml")
relocate("com.fasterxml.jackson")
relocate("com.google.gson")
relocate("net.raphimc.minecraftauth")

repositories {
    mavenCentral()
maven {
		name 'Terraformers'
		url 'https://repo.maven.apache.org/maven2/pe/pi/sctp4j/1.0.7-SNAPSHOT/maven-metadata.xml'
	}
maven {
		name 'f'
		url 'https://repo.maven.apache.org/maven2/pe/pi/sctp4j/1.0.7-SNAPSHOT/sctp4j-1.0.7-SNAPSHOT.pom'
	}
}

dependencies {
    api(project(":core"))
    api(libs.bundles.jackson)
    compileOnly(libs.bundles.geyser)
}

sourceSets {
    main {
        blossom {
            val info = GitInfo(indraGit)
            resources {
                property("version", info.version)
            }
        }
    }
}

nameJar("MCXboxBroadcastExtension")

description = "bootstrap-geyser"

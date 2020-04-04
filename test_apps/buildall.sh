export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

cd sb_jpa_hikari_jdbc
mvn clean package
mv target/*.jar ..
cd ..

cd sb_webflux_hikari_jdbc
mvn clean package
mv target/*.jar ..
cd ..

cd sb_jpa_r2dbcpool_r2dbc
mvn clean package
mv target/*.jar ..
cd ..

cd sb_webflux_r2dbcpool_r2dbc
mvn clean package
mv target/*.jar ..
cd ..




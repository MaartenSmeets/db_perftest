package nl.amis.smeetsm;

public class R2DBCURLSplitter {
    private String driverName;
    private String host;
    private Integer port;
    private String database;
    private String params;

    @Override
    public String toString() {
        return "R2DBCURLSplitter{" +
                "driverName='" + driverName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", params='" + params + '\'' +
                '}';
    }

    public R2DBCURLSplitter(String r2dbcUrl) {
        int pos, pos1, pos2;
        String connUri;

        if (r2dbcUrl == null || !r2dbcUrl.startsWith("r2dbc:")
                || (pos1 = r2dbcUrl.indexOf(':', 6)) == -1)
            throw new IllegalArgumentException("Invalid R2DBC url.");

        driverName = r2dbcUrl.substring(6, pos1);
        if ((pos2 = r2dbcUrl.indexOf(';', pos1)) == -1) {
            connUri = r2dbcUrl.substring(pos1 + 1);
        } else {
            connUri = r2dbcUrl.substring(pos1 + 1, pos2);
            params = r2dbcUrl.substring(pos2 + 1);
        }

        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                host = connUri.substring(2, pos);
                database = connUri.substring(pos + 1);

                if ((pos = getHost().indexOf(':')) != -1) {
                    port = Integer.valueOf(getHost().substring(pos + 1));
                    host = getHost().substring(0, pos);
                }
            }
        } else {
            database = connUri;
        }
    }

    public String getDriverName() {
        return driverName;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getParams() {
        return params;
    }
}

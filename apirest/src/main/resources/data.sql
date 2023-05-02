set schema PUBLIC;

INSERT INTO history (endpoint_url, response, status) VALUES
                 ('http://tenpo.com/applyPercentage/1?1', '(1 + 1) * 0.10 : 0,2', 'OK'),
                 ('http://tenpo.com/applyPercentage/1?1', '(1 + 1) * 0.15 : 0,3', 'OK'),
                 ('http://tenpo.com/applyPercentage/1?1', '(1 + 1) * 0.20 : 0,4', 'OK')

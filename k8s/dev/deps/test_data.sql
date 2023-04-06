INSERT INTO daily_horoscope (zodiac_sign, "date", "text")
SELECT signs,
       genDate,
       'As the sign most associated with communication, you know there are many forms of expression, from active to passive, verbal to non, and so on.'
FROM generate_series(CURRENT_DATE - INTERVAL '50 years', CURRENT_DATE + INTERVAL '5 years', '1 day') genDate
         CROSS JOIN unnest(ARRAY ['Aries','Taurus','Gemini','Cancer','Leo','Virgo','Libra','Scorpio','Sagittarius','Capricorn','Aquarius']) signs
ON CONFLICT ON CONSTRAINT uniq_daily_horoscope_date_sign DO NOTHING;

INSERT INTO weekly_horoscope(zodiac_sign, week_of_year, "text")
SELECT signs,
       TO_CHAR(genDate, 'YYYY-WW'),
       'As the sign most associated with communication, you know there are many forms of expression, from active to passive, verbal to non, and so on.'
FROM generate_series(CURRENT_DATE - INTERVAL '50 years', CURRENT_DATE + INTERVAL '5 years', '1 week') genDate
         CROSS JOIN unnest(ARRAY ['Aries','Taurus','Gemini','Cancer','Leo','Virgo','Libra','Scorpio','Sagittarius','Capricorn','Aquarius']) signs
ON CONFLICT ON CONSTRAINT uniq_weekly_horoscope_week_sign DO NOTHING;

PGDMP                      |            noita    16.3    16.3     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16452    noita    DATABASE     y   CREATE DATABASE noita WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE noita;
                postgres    false            �            1259    16490 
   alchemists    TABLE     e   CREATE TABLE public.alchemists (
    id integer NOT NULL,
    name character varying(32) NOT NULL
);
    DROP TABLE public.alchemists;
       public         heap    postgres    false            �            1259    16489    alchemists_id_seq    SEQUENCE     �   CREATE SEQUENCE public.alchemists_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.alchemists_id_seq;
       public          postgres    false    220            �           0    0    alchemists_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.alchemists_id_seq OWNED BY public.alchemists.id;
          public          postgres    false    219            �            1259    16468    spells    TABLE     �   CREATE TABLE public.spells (
    id integer NOT NULL,
    name character varying(32) NOT NULL,
    type character varying(32) NOT NULL,
    damage integer NOT NULL,
    mana_cost integer NOT NULL,
    id_wand integer NOT NULL
);
    DROP TABLE public.spells;
       public         heap    postgres    false            �            1259    16467    spells_id_seq    SEQUENCE     �   CREATE SEQUENCE public.spells_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.spells_id_seq;
       public          postgres    false    216            �           0    0    spells_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.spells_id_seq OWNED BY public.spells.id;
          public          postgres    false    215            �            1259    16475    wands    TABLE     �   CREATE TABLE public.wands (
    id integer NOT NULL,
    mana_max integer NOT NULL,
    mana_charge_speed integer NOT NULL,
    id_alchemist integer NOT NULL
);
    DROP TABLE public.wands;
       public         heap    postgres    false            �            1259    16474    wands_id_seq    SEQUENCE     �   CREATE SEQUENCE public.wands_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.wands_id_seq;
       public          postgres    false    218            �           0    0    wands_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.wands_id_seq OWNED BY public.wands.id;
          public          postgres    false    217            &           2604    16493    alchemists id    DEFAULT     n   ALTER TABLE ONLY public.alchemists ALTER COLUMN id SET DEFAULT nextval('public.alchemists_id_seq'::regclass);
 <   ALTER TABLE public.alchemists ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    220    220            $           2604    16471 	   spells id    DEFAULT     f   ALTER TABLE ONLY public.spells ALTER COLUMN id SET DEFAULT nextval('public.spells_id_seq'::regclass);
 8   ALTER TABLE public.spells ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    216    216            %           2604    16478    wands id    DEFAULT     d   ALTER TABLE ONLY public.wands ALTER COLUMN id SET DEFAULT nextval('public.wands_id_seq'::regclass);
 7   ALTER TABLE public.wands ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    217    218            �          0    16490 
   alchemists 
   TABLE DATA           .   COPY public.alchemists (id, name) FROM stdin;
    public          postgres    false    220   �       �          0    16468    spells 
   TABLE DATA           L   COPY public.spells (id, name, type, damage, mana_cost, id_wand) FROM stdin;
    public          postgres    false    216   N       �          0    16475    wands 
   TABLE DATA           N   COPY public.wands (id, mana_max, mana_charge_speed, id_alchemist) FROM stdin;
    public          postgres    false    218   !       �           0    0    alchemists_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.alchemists_id_seq', 5, true);
          public          postgres    false    219            �           0    0    spells_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.spells_id_seq', 19, true);
          public          postgres    false    215            �           0    0    wands_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.wands_id_seq', 7, true);
          public          postgres    false    217            .           2606    16495    alchemists alchemist_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.alchemists
    ADD CONSTRAINT alchemist_pkey PRIMARY KEY (id);
 C   ALTER TABLE ONLY public.alchemists DROP CONSTRAINT alchemist_pkey;
       public            postgres    false    220            *           2606    16473    spells spells_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.spells
    ADD CONSTRAINT spells_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.spells DROP CONSTRAINT spells_pkey;
       public            postgres    false    216            ,           2606    16480    wands wand_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.wands
    ADD CONSTRAINT wand_pkey PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.wands DROP CONSTRAINT wand_pkey;
       public            postgres    false    218            '           2606    16585 #   wands wands_mana_charge_speed_check    CHECK CONSTRAINT     v   ALTER TABLE public.wands
    ADD CONSTRAINT wands_mana_charge_speed_check CHECK ((mana_charge_speed >= 0)) NOT VALID;
 H   ALTER TABLE public.wands DROP CONSTRAINT wands_mana_charge_speed_check;
       public          postgres    false    218    218            (           2606    16580    wands wands_mana_max_check    CHECK CONSTRAINT     d   ALTER TABLE public.wands
    ADD CONSTRAINT wands_mana_max_check CHECK ((mana_max >= 0)) NOT VALID;
 ?   ALTER TABLE public.wands DROP CONSTRAINT wands_mana_max_check;
       public          postgres    false    218    218            /           2606    16501    spells fk_spell_wand    FK CONSTRAINT     }   ALTER TABLE ONLY public.spells
    ADD CONSTRAINT fk_spell_wand FOREIGN KEY (id_wand) REFERENCES public.wands(id) NOT VALID;
 >   ALTER TABLE ONLY public.spells DROP CONSTRAINT fk_spell_wand;
       public          postgres    false    218    216    4652            0           2606    16506    wands fk_wand_alchemist    FK CONSTRAINT     �   ALTER TABLE ONLY public.wands
    ADD CONSTRAINT fk_wand_alchemist FOREIGN KEY (id_alchemist) REFERENCES public.alchemists(id) NOT VALID;
 A   ALTER TABLE ONLY public.wands DROP CONSTRAINT fk_wand_alchemist;
       public          postgres    false    4654    218    220            �   R   x���	�0��}���q��U���!� ��;�m��SHH�X��:K��D�R�tb�Po��W��=7QZ�b{ґݘ�+D��G/&      �   �  x���_J�P���"Pr�F�{q1m�E���A� ����M0�&nafG��SA�=$���|��L����6�^�Z�<��k�����-���6�J�RH�{��Yk����~��!n��Ľ�`_�[~�C�ĎD�!͸������K,d
� WZjgSo"e��ΐ�V��>��M��Qȃ}�8��s�r�;�W\�*K	Ǆvv�KF���'z�%��>]�.�"���6qz�@?��G�3{l�xי>��bA�t7Bk@�v�cbS9n@a,)Zڑ��me��B�d�qK�4�گX�.Ę��B#ӥw�H������+Ǯa�,�&��D`�ƕ5ӯ���k�Or��ڎ2
MHB(vlF�f���'D��<�,�U6g[j�C&�&-M�w�DV+�nή蝯����ؙ�AZ����s���+��B�^�I      �   A   x����@�PL�>��I�u���A!5�D��DEa;��}]-�&��&�8�mMY߇��!     
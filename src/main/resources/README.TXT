MAKE SURE TO COPY ALL FILES IN SRC/MAIN/RESOURCES/ TO SRC/MAIN/WEBAPP otherwise digital ocean Tomcat 
is unable to get css, js, etc. ONLY items that have been programmatically accessed (tomcat superuser)
can access folder /WEB-INF/classes/ where the resources normally reside, and copying to webapp makes
them fully public.  
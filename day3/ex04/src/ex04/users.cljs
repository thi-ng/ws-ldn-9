(ns ex04.users)

(defn profile
  [route]
  [:div
   [:h1 "User: " (get-in route [:params :id])]])
